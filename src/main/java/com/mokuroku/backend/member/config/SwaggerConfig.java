package com.mokuroku.backend.member.config;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Everywhere API")
                        .version("v1")
                        .description("읽는곳곳 API 명세서"))
                .addServersItem(new Server()
                        .url("/api")
                        .description("Default Server")
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springdoc-public")
                .pathsToMatch("/**")
                .build();
    }
}

// http://localhost:8080/api/swagger-ui.html
// http://localhost:8080/api/v3/api-docs