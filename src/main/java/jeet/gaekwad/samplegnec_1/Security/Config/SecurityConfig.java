package jeet.gaekwad.samplegnec_1.Security.Config;

import jeet.gaekwad.samplegnec_1.Security.JWT.Filter.JwtAuthenticationFilter;
import jeet.gaekwad.samplegnec_1.Security.JWT.Filter.JwtRefreshTokenFilter;
import jeet.gaekwad.samplegnec_1.Security.JWT.ProviderList.JwtAuthenticationProvider;
import jeet.gaekwad.samplegnec_1.Security.JWT.JwtUtils;
import jeet.gaekwad.samplegnec_1.Security.JWT.Filter.JwtValidationFilter;
import jeet.gaekwad.samplegnec_1.Security.Oauth.CustomOAuthHandler;
import jeet.gaekwad.samplegnec_1.Service.AccountService.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AccountServiceImpl accountService1;

    private UserDetailsService userDetailsService;
    private JwtUtils jwtUtils;
    private CustomOAuthHandler customOAuthHandler;

    public SecurityConfig(UserDetailsService userDetailsService, JwtUtils jwtUtils , CustomOAuthHandler customOAuthHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.customOAuthHandler = customOAuthHandler;
    }

   @Bean
    public PasswordEncoder passwordEncoder() { // For decoding and encoding the password
       return new BCryptPasswordEncoder();
   }

    @Bean
    public AuthenticationManager authenticationManager() { // custom authentication manager for list of providers
        return new ProviderManager(Arrays.asList(
                 daoAuthenticationProvider() //For checking the username and password
                ,jwtAuthenticationProvider() //For validating the Jwt token everytime
        ));
    }
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() { // custom JwtAuthentication provider for validating JWT
        return new JwtAuthenticationProvider(jwtUtils, accountService1);
    }
    @Bean
   public DaoAuthenticationProvider daoAuthenticationProvider() { // for validating the username and password from DB
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountService1);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
   }
   @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

       JwtAuthenticationFilter jwtAuthenticationFilter = new
               JwtAuthenticationFilter(authenticationManager(), jwtUtils); // For Jwt Authentication

       JwtValidationFilter jwtValidationFilter = new
               JwtValidationFilter(authenticationManager()); // For Jwt Validation

       JwtRefreshTokenFilter jwtRefreshTokenFilter = new
               JwtRefreshTokenFilter(jwtUtils,authenticationManager()); // For refreshing the Token


       http.authorizeHttpRequests(auth -> auth
               .requestMatchers(
                       "/login",
                       "/logout",
                       "/v1/register",
                       "/mimic3/tts" ,
                       "/api/auth/**",           // Your existing signup/login APIs
                       "/oauth2/**",              // OAuth2 login flow URLs
                       "/login/**",
                       "/v3/api-docs/**",
                       "/swagger-ui/**",
                       "/swagger-ui.html",
                       "/v3/api-docs.yaml",// Spring internal login URLs
                       "/error" ).permitAll()
                       .requestMatchers("/v1/accounts").hasRole("ADMIN")
               .anyRequest().authenticated())
               .csrf(csrf -> csrf.disable())
               .sessionManagement(session ->
                       session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// stateless for JWT
               .oauth2Login(oauth2 -> oauth2.successHandler(customOAuthHandler))
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
               .addFilterAfter(jwtValidationFilter, JwtAuthenticationFilter.class)
               .addFilterAfter(jwtRefreshTokenFilter, JwtValidationFilter.class);


       return http.build();
   }
}
