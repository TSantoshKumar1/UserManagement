package com.service.demo.swaggerConfig;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI customOpenApi(){

        return new OpenAPI().info(new Info().title("demo").version("1.0").description("swager api for demo jwt").contact(new Contact().name("john").email("john@gmail.com").url("https://yourportfolio.com")));
    }
}
