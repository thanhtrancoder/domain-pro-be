package thanhtrancoder.domain_pro_be.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import thanhtrancoder.domain_pro_be.common.entity.ResponseCustom;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding("UTF-8");

        ResponseCustom<Objects> responseCustom = new ResponseCustom<>();
        responseCustom.setTimestamp(LocalDateTime.now());
        responseCustom.setStatus(HttpServletResponse.SC_FORBIDDEN);
        responseCustom.setMessage("You do not have permission to access this resource.");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // handle LocalDateTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(response.getOutputStream(), responseCustom);
        response.getOutputStream().flush();
    }
}

