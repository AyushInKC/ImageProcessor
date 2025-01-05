package com.JavaJunkie.ImageProcessor.Security;
import com.JavaJunkie.ImageProcessor.Utils.JWTUtility;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtility jwtUtility;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7).trim();

            try {
                if (jwtUtility.validateToken(token, null)) {
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(jwtUtility.getSecretKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    claims.getSubject(), null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                            );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    logger.error("JWT validation failed.");
                }
            } catch (Exception e) {
                logger.error("JWT validation failed: ", e);
            }
        }

        filterChain.doFilter(request, response);
    }

}
