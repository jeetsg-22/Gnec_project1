package jeet.gaekwad.samplegnec_1.Controller.SecurityDocumentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Endpoints", description = "APIs related to login, logout, JWT authentication, token refresh, and OAuth2 login via Google")
@RestController
@RequestMapping("/api/auth-docs") // Only for grouping, real URLs are handled separately
public class SecurityAuthDocumentationController {

    @Operation(summary = "Login (Spring Security Form Login)", description = "Login with username and password using form parameters. Automatically handled by Spring Security.")
    @PostMapping("/login")
    public void login() {
        // Handled by Spring Security internally
    }

    @Operation(summary = "Logout (Spring Security)", description = "Logout the currently authenticated user. Handled automatically by Spring Security.")
    @PostMapping("/logout")
    public void logout() {
        // Handled by Spring Security internally
    }

    @Operation(summary = "Generate JWT Token (Custom)", description = "Authenticate with username and password to receive a JWT access token. Custom implementation.")
    @PostMapping("/generate-token")
    public void generateToken() {
        // Handled by your JWT Authentication Service
    }

    @Operation(summary = "Refresh JWT Token (Custom)", description = "Use this endpoint to refresh your JWT access token using a valid refresh token. Custom implementation.")
    @PostMapping("/refresh-token")
    public void refreshToken() {
        // Handled by your Refresh Token Service
    }

    @Operation(summary = "OAuth2 Login via Google", description = "Login using Google OAuth2. Redirects user to Google's consent page and retrieves user info upon success. Handled by Spring Security.")
    @GetMapping("/oauth2/authorization/google")
    public void oauth2LoginGoogle() {
        // Handled automatically by Spring Security OAuth2
    }

    @Operation(summary = "OAuth2 Authorization Endpoint", description = "General OAuth2 authorization endpoint for starting external provider login flows (like Google, Github, etc.).")
    @GetMapping("/oauth2/authorize")
    public void oauth2Authorize() {
        // Internal Spring OAuth2 processing
    }

    @Operation(summary = "OAuth2 Token Endpoint", description = "Exchanges authorization code for access and refresh tokens. Part of OAuth2 standard flow. Handled internally.")
    @PostMapping("/oauth2/token")
    public void oauth2Token() {
        // Handled internally
    }
}
