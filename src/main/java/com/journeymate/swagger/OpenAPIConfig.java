package com.journeymate.swagger;

import com.journeymate.utils.ITag;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "JourneyMate API",
        version = "1.0",
        description = "JourneyMate Backend REST API Documentation",
        contact = @Contact(
            name = "JourneyMate Support",
            email = "support@journeymate.com"
        )
    ),
    servers = {
        @Server(
            description = "Local Development",
            url = "http://localhost:8081"
        )
    },
    security = {
        @SecurityRequirement(name = ITag.SECURITY_SCHEME_NAME)
    }
)
@SecurityScheme(
    name = ITag.SECURITY_SCHEME_NAME,
    description = ITag.SECURITY_SCHEME_DESCRIPTION,
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = ITag.SECURITY_SCHEME_BEARER_FORMAT,
    in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {}