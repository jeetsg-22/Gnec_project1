package jeet.gaekwad.samplegnec_1.Config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("My API Documentation")
                        .description("This is the official backend API for authentication, audio file management, profile photo management, notebook handling, and AI features.")
                        .version("1.0.0")
                );
    }


}
