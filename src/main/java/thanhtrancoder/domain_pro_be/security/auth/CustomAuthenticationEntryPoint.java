package thanhtrancoder.domain_pro_be.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Cấu hình response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");

        ResponseCustom<Objects> responseCustom = new ResponseCustom<>();
        responseCustom.setTimestamp(LocalDateTime.now());
        responseCustom.setStatus(HttpServletResponse.SC_FORBIDDEN);
        responseCustom.setMessage("Bạn không có quyền truy cập tài nguyên này.");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // xử lý LocalDateTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(response.getOutputStream(), responseCustom);
        response.getOutputStream().flush();
    }
}
