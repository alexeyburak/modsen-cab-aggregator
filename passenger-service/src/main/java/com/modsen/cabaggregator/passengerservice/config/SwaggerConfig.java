package com.modsen.cabaggregator.passengerservice.config;

import com.modsen.cabaggregator.passengerservice.util.Constants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Passenger Service API",
                version = "v1.0",
                contact = @Contact(
                        name = "Alexey Burak",
                        email = "burakalexey@yahoo.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8081/", description = "Development server")
        },
        tags = {
                @Tag(name = Constants.PASSENGERS, description = "API for passengers CRUD operations"),
                @Tag(name = Constants.RATINGS, description = "API for ratings CRUD operations")
        }
)
public class SwaggerConfig {
}
