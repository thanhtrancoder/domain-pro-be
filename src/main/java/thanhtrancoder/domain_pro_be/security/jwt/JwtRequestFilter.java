package thanhtrancoder.domain_pro_be.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import thanhtrancoder.domain_pro_be.common.exceptions.LoginSessionExpired;
import thanhtrancoder.domain_pro_be.security.auth.CustomUserDetails;
import thanhtrancoder.domain_pro_be.security.auth.CustomUserDetailsService;
import thanhtrancoder.domain_pro_be.common.exceptions.CustomException;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            String jwt = null;
            String username = null;
            Integer tokenVersion = null;

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwt = authHeader.substring(7);
                try {
                    username = jwtUtil.extractEmail(jwt);
                    tokenVersion = jwtUtil.extractTokenVersion(jwt);
                } catch (Exception e) {
                    // Log lỗi nếu cần
                }
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = userDetailsService.loadUserByEmail(username);

                if (!jwtUtil.validateToken(jwt)) {
                    throw new LoginSessionExpired("Phiên đăng nhập đã hết hạn");
                }

                if (tokenVersion != userDetails.getTokenVersion()) {
                    throw new LoginSessionExpired("Phiên đăng nhập đã hết hạn");
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);
        } catch (LoginSessionExpired ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
