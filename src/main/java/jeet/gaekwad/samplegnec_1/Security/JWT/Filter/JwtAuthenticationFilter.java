package jeet.gaekwad.samplegnec_1.Security.JWT.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jeet.gaekwad.samplegnec_1.Security.JWT.JwtUtils;
import jeet.gaekwad.samplegnec_1.Security.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().equals("/generate-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        LoginRequest loginRequest = mapper.readValue(request.getReader(), LoginRequest.class);

        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authResult =
                authenticationManager.authenticate(authenticationToken);
        if (authResult.isAuthenticated()) {
            String token = jwtUtils.generateToken(authResult.getName(),15);
            response.addHeader("Authorization", "Bearer " + token);
            
            String refreshToken = jwtUtils.generateToken(authResult.getName(),7 * 24 * 60);//7 day time
            Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true);
            refreshTokenCookie.setPath("/refresh-token"); //Api to refresh Jwt
            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);//7 day expiry
            response.addCookie(refreshTokenCookie);
        }

    }
}
