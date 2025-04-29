package jeet.gaekwad.samplegnec_1.Security.JWT.ProviderList;

import jeet.gaekwad.samplegnec_1.Security.JWT.JwtAuthenticationToken;
import jeet.gaekwad.samplegnec_1.Security.JWT.JwtUtils;
import jeet.gaekwad.samplegnec_1.Service.AccountService.AccountServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationProvider  implements AuthenticationProvider {

    private JwtUtils jwtUtils;
    private AccountServiceImpl accountService;

    public JwtAuthenticationProvider(JwtUtils jwtUtils, AccountServiceImpl accountService) {
        this.jwtUtils = jwtUtils;
        this.accountService = accountService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        String token = jwtToken.getToken();
        String username = jwtUtils.validateAndExtractUsername(token);
        if (username == null) {
            throw new BadCredentialsException("Invalid Jwt token");
        }
        UserDetails userDetails = accountService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
