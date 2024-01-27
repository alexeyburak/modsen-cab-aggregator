package com.modsen.cabaggregator.driverservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Driver Service API",
                version = "v1.0",
                contact = @Contact(
                        name = "Alexey Burak",
                        email = "burakalexey@yahoo.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8082/", description = "Development server")
        },
        tags = {
                @Tag(name = "Drivers", description = "API for drivers CRUD operations"),
                @Tag(name = "Rating", description = "API for ratings CRUD operations")
        }
)
public class SwaggerConfig {
}
