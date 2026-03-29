package org.springboot.urlshorteningservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record CreateUrlRequest(

        @NotBlank(message = "URL cannot be blank")
        @Size(max = 2048, message = "URL cannot be more than 2048 characters")
        @Pattern(
                regexp = "^https://.*",
                message = "URL must start with https"
        )
        @URL(message = "URL must be a valid URL")
        String url
) {}
