package thanhtrancoder.domain_pro_be.module.momo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;
import thanhtrancoder.domain_pro_be.common.exceptions.ExternalException;
import thanhtrancoder.domain_pro_be.module.momo.dto.CollectionLinkDto;
import thanhtrancoder.domain_pro_be.module.momo.dto.CollectionLinkRequest;
import thanhtrancoder.domain_pro_be.module.momo.dto.CollectionLinkResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

@Service
public class MoMoService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ModelMapper modelMapper;

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

    public CollectionLinkResponse createCollectionLink(CollectionLinkRequest req) {
        String requestUrl = endpoint + "/create";
        String requestId = UUID.randomUUID().toString();

        CollectionLinkDto collectionLinkDto = modelMapper.map(req, CollectionLinkDto.class);
        collectionLinkDto.setPartnerCode(partnerCode);
        collectionLinkDto.setStoreId(storeId);
        collectionLinkDto.setRedirectUrl(redirectUrl);
        collectionLinkDto.setIpnUrl(ipnUrl);
        collectionLinkDto.setRequestId(requestId);
        collectionLinkDto.setRequestType("payWithMethod");

        // Tạo extraData nếu cần (ví dụ base64 của JSON)
        if (collectionLinkDto.getExtraData() == null) {
            collectionLinkDto.setExtraData("");
        }

        // Tạo signature: theo thứ tự a → z các key
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

        return resp.getBody();
    }


    public void handleIpn(Map<String, Object> ipnPayload) {
        if (!validateSignature(ipnPayload)) {
            throw new CustomException("Xác thực thất bại.");
        }
    }

    private boolean validateSignature(Map<String, Object> payload) {
        String partnerCode = asString(payload.get("partnerCode"));
        String orderId = asString(payload.get("orderId"));
        String requestId = asString(payload.get("requestId"));
        String amount = asString(payload.get("amount"));
        String orderInfo = asString(payload.get("orderInfo"));
        String orderType = asString(payload.get("orderType"));
        String transId = asString(payload.get("transId"));
        String resultCode = asString(payload.get("resultCode"));
        String message = asString(payload.get("message"));
        String payType = asString(payload.get("payType"));
        String responseTime = asString(payload.get("responseTime"));
        String extraData = asString(payload.get("extraData"));
        String signatureMoMo = asString(payload.get("signature"));

        String raw = "accessKey=" + accessKey +
                "&amount=" + amount +
                "&extraData=" + extraData +
                "&message=" + message +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&orderType=" + orderType +
                "&partnerCode=" + partnerCode +
                "&payType=" + payType +
                "&requestId=" + requestId +
                "&responseTime=" + responseTime +
                "&resultCode=" + resultCode +
                "&transId=" + transId;

        String signatureCalc = hmacSha256(raw, secretKey);

        return signatureCalc.equals(signatureMoMo);
    }

    private String asString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    private String hmacSha256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            // chuyển sang hex (lowercase)
            StringBuilder sb = new StringBuilder(raw.length * 2);
            for (byte b : raw) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Không thể tính HMAC SHA256", e);
        }
    }
}
