package thanhtrancoder.domain_pro_be.module.momo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.ExternalException;
import thanhtrancoder.domain_pro_be.common.exceptions.QueryException;
import thanhtrancoder.domain_pro_be.common.utils.ConstantValue;
import thanhtrancoder.domain_pro_be.module.account.AccountEntity;
import thanhtrancoder.domain_pro_be.module.account.AccountService;
import thanhtrancoder.domain_pro_be.module.cart.CartService;
import thanhtrancoder.domain_pro_be.module.domainName.DomainNameService;
import thanhtrancoder.domain_pro_be.module.domainName.dto.DomainNameDto;
import thanhtrancoder.domain_pro_be.module.email.EmailService;
import thanhtrancoder.domain_pro_be.module.momo.dto.*;
import thanhtrancoder.domain_pro_be.module.notification.NotificationService;
import thanhtrancoder.domain_pro_be.module.notification.dto.NotificationDto;
import thanhtrancoder.domain_pro_be.module.orderItem.OrderItemService;
import thanhtrancoder.domain_pro_be.module.orderItem.dto.OrderItemDto;
import thanhtrancoder.domain_pro_be.module.orders.OrderService;
import thanhtrancoder.domain_pro_be.module.orders.dto.OrderDto;
import thanhtrancoder.domain_pro_be.module.paymentBills.PaymentBillEntity;
import thanhtrancoder.domain_pro_be.module.paymentBills.PaymentBillService;
import thanhtrancoder.domain_pro_be.module.paymentBills.dto.PaymentBillDto;
import thanhtrancoder.domain_pro_be.module.vouchers.VoucherService;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MoMoService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PaymentBillService paymentBillService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DomainNameService domainNameService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailService;

    @Value("${dev.momo.endpoint}")
    private String endpoint;
    @Value("${dev.momo.partner-code}")
    private String partnerCode;
    @Value("${dev.momo.access-key}")
    private String accessKey;
    @Value("${dev.momo.secret-key}")
    private String secretKey;
    @Value("${dev.momo.your-store-id}")
    private String storeId;
    @Value("${dev.momo.redirect-url}")
    private String redirectUrl;
    @Value("${dev.momo.ipn-url}")
    private String ipnUrl;

    public MoMoRes createCollectionLink(CollectionLinkRequest req, Long accountId) {
        OrderDto orderDto = orderService.getDetail(Long.valueOf(req.getOrderId()), accountId);
        if (orderDto.getStatus() == ConstantValue.ORDER_PAID) {
            throw new CustomException("The order has already been paid.");
        }

        // create user info
        UserInfo userInfo = new UserInfo();
        userInfo.setName(orderDto.getFullname());
        userInfo.setPhoneNumber(orderDto.getPhone());
        userInfo.setEmail(orderDto.getEmail());

        // create item
        List<Item> items = new ArrayList<>();
        Page<OrderItemDto> orderItemList = orderItemService.getAllByOrderId(
                orderDto.getOrderId(),
                Pageable.unpaged()
        );
        int idIndex = 1;
        for (OrderItemDto orderItemDto : orderItemList) {
            Item item = new Item();
            item.setId(String.valueOf(idIndex));
            item.setName("Domain: "
                    + orderItemDto.getDomainName()
                    + orderItemDto.getDomainExtend()
                    + " (" + orderItemDto.getPeriod() + " years)"
            );
            item.setDescription("Duration: " + orderItemDto.getPeriod() + " years");
//            item.setPrice(orderItemDto.getPrice());
//            item.setCurrency("VND");
            item.setQuantity(1);
//            item.setTotalPrice(orderItemDto.getPrice());

            items.add(item);
            idIndex++;
        }

        // create requestId
        String requestUrl = endpoint + "/create";
        String requestId = UUID.randomUUID().toString();

        CollectionLinkDto collectionLinkDto = modelMapper.map(req, CollectionLinkDto.class);
        collectionLinkDto.setPartnerCode(partnerCode);
        collectionLinkDto.setStoreId(storeId);
        collectionLinkDto.setAmount(orderDto.getTotalPrice());
        collectionLinkDto.setOrderInfo("DomainPro - Order payment");
        collectionLinkDto.setRedirectUrl(redirectUrl);
        collectionLinkDto.setIpnUrl(ipnUrl);
        collectionLinkDto.setRequestId(requestId);
        collectionLinkDto.setItems(items);
        collectionLinkDto.setUserInfo(userInfo);
        collectionLinkDto.setRequestType("payWithMethod");

        // Create extraData if needed (e.g., base64 of JSON)
        if (collectionLinkDto.getExtraData() == null) {
            collectionLinkDto.setExtraData("");
        }

        // Create signature: keys in a to z order
        String raw = "accessKey=" + accessKey
                + "&amount=" + collectionLinkDto.getAmount()
                + "&extraData=" + collectionLinkDto.getExtraData()
                + "&ipnUrl=" + ipnUrl
                + "&orderId=" + collectionLinkDto.getOrderId()
                + "&orderInfo=" + collectionLinkDto.getOrderInfo()
                + "&partnerCode=" + partnerCode
                + "&redirectUrl=" + redirectUrl
                + "&requestId=" + requestId
                + "&requestType=payWithMethod";

        String signature = hmacSha256(raw, secretKey);
        collectionLinkDto.setSignature(signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CollectionLinkDto> httpReq = new HttpEntity<>(collectionLinkDto, headers);

        ResponseEntity<CollectionLinkResponse> resp = restTemplate
                .postForEntity(requestUrl, httpReq, CollectionLinkResponse.class);
        if (resp.getBody().getResultCode() != 0) {
            throw new ExternalException("Momo error", resp.getBody());
        }

        MoMoRes moMoRes = new MoMoRes();
        moMoRes.setPayUrl(resp.getBody().getPayUrl());
        moMoRes.setShortLink(resp.getBody().getShortLink());

        return moMoRes;
    }

    @Transactional
    public void processPayment(MoMoReq moMoReq) {
        try {
            // Save into paymentBills
            PaymentBillDto paymentBillDto = modelMapper.map(moMoReq, PaymentBillDto.class);
            paymentBillDto.setPaymentMethodId(1L);
            paymentBillService.create(paymentBillDto, 0L);

            // Update order
            OrderDto orderDto = orderService.updateOrderStatus(
                    Long.valueOf(paymentBillDto.getOrderId()),
                    ConstantValue.ORDER_PAID,
                    0L
            );

            // Create domainName and get domains
            Map<String, Integer> domains = new HashMap<>();
            Page<OrderItemDto> orderItemList = orderItemService.getAllByOrderId(
                    Long.valueOf(paymentBillDto.getOrderId()),
                    Pageable.unpaged()
            );
            orderItemList.forEach(orderItem -> {
                DomainNameDto domainNameDto = new DomainNameDto();
                domainNameDto.setDomainName(orderItem.getDomainName());
                domainNameDto.setDomainExtend(orderItem.getDomainExtend());
                domainNameDto.setDomainExtendId(orderItem.getDomainExtendId());
                domainNameDto.setIsAutoRenewal(false);
                domainNameDto.setRegisterAt(LocalDateTime.now());
                domainNameDto.setExpiresAt(LocalDateTime.now().plusYears(orderItem.getPeriod()));
                domainNameDto.setIsBlock(false);
                domainNameDto.setDnsProvider("CloudDNS");
                domainNameDto.setAccountId(orderDto.getAccountId());

                domainNameService.create(domainNameDto, orderDto.getAccountId());

                domains.put(orderItem.getDomainName() + orderItem.getDomainExtend(), orderItem.getPeriod());
            });

            // Delete cart item
            cartService.deleteCartItem(orderDto.getAccountId());

            // Update voucher
            if (orderDto.getDiscountCode() != null && !orderDto.getDiscountCode().isEmpty()) {
                voucherService.updateUsage(orderDto.getDiscountCode());
            }

            // Create notification
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setType(ConstantValue.NOTIFICATION_TYPE_SUCCESS);
            notificationDto.setTitle("Payment for order " + paymentBillDto.getOrderId() + " was successful");
            notificationDto.setContent("");
            notificationService.systemCreate(orderDto.getAccountId(), notificationDto);

            // Send email
            emailService.orderPaymentSuccess(
                    orderDto.getAccountId(),
                    orderDto.getOrderId().toString(),
                    domains
            );
        } catch (Exception e) {
            throw new QueryException("An error occurred during payment", e);
        }
    }

    public void handlePayment(MoMoReq moMoReq) {
        if (!validateSignature(moMoReq)) {
            throw new CustomException("Authentication failed.");
        }
        processPayment(moMoReq);
    }

    public CheckPaymentRes checkPayment(MoMoReq moMoReq) {
        if (!validateSignature(moMoReq)) {
            throw new CustomException("Authentication failed.");
        }

        moMoReq.setSignature(null);
        PaymentBillEntity paymentBillEntity = paymentBillService.getDetailByOrderIdOrNull(moMoReq.getOrderId());
        if (paymentBillEntity == null) {
            processPayment(moMoReq);
        } else {
            try {
                MoMoReq momoBill = modelMapper.map(paymentBillEntity, MoMoReq.class);
                if (!moMoReq.equals(momoBill)) {
                    processPayment(moMoReq);
                }
            } catch (Exception e) {
                processPayment(moMoReq);
            }
        }

        if (paymentBillEntity == null) {
            paymentBillEntity = paymentBillService.getDetailByOrderIdOrNull(moMoReq.getOrderId());
        }

        CheckPaymentRes res = new CheckPaymentRes();
        res.setOrderId(paymentBillEntity.getOrderId());
        res.setAmount(Long.valueOf(paymentBillEntity.getAmount()));
        res.setCreatedAt(paymentBillEntity.getCreatedAt());
        return res;
    }

    private boolean validateSignature(MoMoReq moMoReq) {
        String raw = "accessKey=" + accessKey +
                "&amount=" + moMoReq.getAmount() +
                "&extraData=" + moMoReq.getExtraData() +
                "&message=" + moMoReq.getMessage() +
                "&orderId=" + moMoReq.getOrderId() +
                "&orderInfo=" + moMoReq.getOrderInfo() +
                "&orderType=" + moMoReq.getOrderType() +
                "&partnerCode=" + moMoReq.getPartnerCode() +
                "&payType=" + moMoReq.getPayType() +
                "&requestId=" + moMoReq.getRequestId() +
                "&responseTime=" + moMoReq.getResponseTime() +
                "&resultCode=" + moMoReq.getResultCode() +
                "&transId=" + moMoReq.getTransId();

        String signatureCalc = hmacSha256(raw, secretKey);

        return signatureCalc.equals(moMoReq.getSignature());
    }

    private String asString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private String hmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // convert to hex (lowercase)
            StringBuilder sb = new StringBuilder(raw.length * 2);
            for (byte b : raw) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to compute HMAC SHA256", e);
        }
    }
}
