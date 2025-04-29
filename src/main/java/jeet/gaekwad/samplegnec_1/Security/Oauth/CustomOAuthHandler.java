package jeet.gaekwad.samplegnec_1.Security.Oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jeet.gaekwad.samplegnec_1.Model.Accounts;
import jeet.gaekwad.samplegnec_1.Repository.AccountRepository;
import jeet.gaekwad.samplegnec_1.Security.JWT.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomOAuthHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtUtils;
    private final AccountRepository accountRepository;

    public CustomOAuthHandler(JwtUtils jwtUtils, AccountRepository accountRepository) {
        this.jwtUtils = jwtUtils;
        this.accountRepository = accountRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<Accounts> accountsOptional = accountRepository.findByEmail(email);

        Accounts accounts;
        if (accountsOptional.isEmpty()) {
            accounts = new Accounts();
            accounts.setEmail(email);
            accounts.setUsername(name);
            accounts.setRole("USER");
            accountRepository.save(accounts);
        } else {
            accounts = accountsOptional.get();
        }

        String jwtToken = jwtUtils.generateToken(email,30);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jwtToken);
    }
}
