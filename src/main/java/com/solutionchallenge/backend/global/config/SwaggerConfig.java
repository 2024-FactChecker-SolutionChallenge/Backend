package com.solutionchallenge.backend.global.config;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

//http://localhost:8080/swagger-ui/index.html
@Configuration
public class SwaggerConfig {
    private final String TITLE = "2024-SolutionChallenge";
    private final String DESCRIPTION = "GDSC 2024-SolutionChallenge Backend API";
    private final String VERSION = "V1.0.0";

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION)
                );
    }

}
