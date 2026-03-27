package org.springboot.urlshorteningservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UrlResponse(UUID id, String shortCode, String url,
                   LocalDateTime createdAt, LocalDateTime updatedAt){}
