package com.librio.backend;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Librio Backend API")
                        .description("API REST pour la gestion des livres, favoris et utilisateurs (projet JEE Spring Boot).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Clément Venot")
                                .email("clement.venot@etu.univ-tours.fr")))
                .externalDocs(new ExternalDocumentation()
                        .description("Code source du projet (Git)")
                        .url("https://github.com/clementvenot/librio"))
                .addServersItem(new Server().url("http://localhost:8080").description("Local"));
    }
}