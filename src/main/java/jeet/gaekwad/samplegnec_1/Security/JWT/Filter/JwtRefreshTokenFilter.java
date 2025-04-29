package jeet.gaekwad.samplegnec_1.Security.JWT.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jeet.gaekwad.samplegnec_1.Security.JWT.JwtAuthenticationToken;
import jeet.gaekwad.samplegnec_1.Security.JWT.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public JwtRefreshTokenFilter(JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().equals("/refresh-token")) {
            filterChain.doFilter(request, response);
            return;
        }
        String refreshToken = extractJwtFromRequest(request);
        if(refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(refreshToken);
        Authentication authResult = authenticationManager.authenticate(authenticationToken);
        if(authResult.isAuthenticated()) {
            String newToken = jwtUtils.generateToken(authResult.getName(),15);
            response.addHeader("Authorization", "Bearer " + newToken);
        }
    }
    private String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }
        return refreshToken;
    }
}
