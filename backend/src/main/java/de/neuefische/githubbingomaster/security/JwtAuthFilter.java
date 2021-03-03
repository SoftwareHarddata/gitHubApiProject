package de.neuefische.githubbingomaster.security;


import de.neuefische.githubbingomaster.service.TimeUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtConfig jwtConfig;
    private final TimeUtils timeUtils;

    public JwtAuthFilter(JwtConfig jwtConfig, TimeUtils timeUtils) {
        this.jwtConfig = jwtConfig;
        this.timeUtils = timeUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null) {
                // check token valid
                Jws<Claims> parsedToken = parseToken(authorizationHeader);
                if (parsedToken.getBody().getExpiration().after(Date.from(timeUtils.now()))) {
                    setSecurityContext(parsedToken);
                }
            }
        } catch (Exception e) {
            logger.warn("failed to parse token", e);
        }
        filterChain.doFilter(request, response);
    }

    private Jws<Claims> parseToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer", "").trim();
        return Jwts.parser().setSigningKey(jwtConfig.getJwtSecret()).parseClaimsJws(token);
    }


    private void setSecurityContext(Jws<Claims> parsedToken) {
        Claims body = parsedToken.getBody();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new AppUser(body.getSubject(), null), null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
