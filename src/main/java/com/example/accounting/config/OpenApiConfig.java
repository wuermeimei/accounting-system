package com.example.accounting.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("记账系统 API")
                        .version("1.0")
                        .description("基于 Spring Boot 2.7.x 的记账系统 REST API 文档")
                        .contact(new Contact()
                                .name("Accounting System")
                                .email("contact@example.com")
                                .url("https://github.com/wuermeimei/accounting-system")
                        )
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")
                        )
                );
    }
}