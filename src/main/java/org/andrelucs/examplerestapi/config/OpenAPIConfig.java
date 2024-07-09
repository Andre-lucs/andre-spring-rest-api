package org.andrelucs.examplerestapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
               .info(new io.swagger.v3.oas.models.info.Info()
                       .title("Book REST API")
                       .version("1.0.0")
                       .description("Book REST API")
                       .license(new io.swagger.v3.oas.models.info.License()
                               .name("Apache 2.0")
                               .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                       .termsOfService("http://swagger.io/terms/")
                       .contact(new io.swagger.v3.oas.models.info.Contact()
                               .name("Swagger")
                               .url("http://swagger.io")
                               .email("support")));
    }

}
