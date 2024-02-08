package com.modsen.cabaggregator.rideservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Ride Service API",
                version = "v1.0",
                contact = @Contact(
                        name = "Alexey Burak",
                        email = "burakalexey@yahoo.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8083/", description = "Development server")
        },
        tags = {
                @Tag(name = "Rides", description = "API for rides CRUD operations"),
                @Tag(name = "Promo codes", description = "API for promo codes CRUD operations")
        }
)
public class SwaggerConfig {
}
