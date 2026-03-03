package org.springboot.urlshorteningservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreateUrlResponse {
    private String shortCode;
    private String shortUrl;
    private LocalDateTime createdAt;
}
