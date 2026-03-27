package org.springboot.urlshorteningservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UrlResponse(UUID id, String url, String shortCode,
                   LocalDateTime createdAt, LocalDateTime updatedAt){}
