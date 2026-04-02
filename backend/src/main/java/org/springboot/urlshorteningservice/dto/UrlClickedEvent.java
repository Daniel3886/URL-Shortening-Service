package org.springboot.urlshorteningservice.dto;

public record UrlClickedEvent(
        String shortCode,
        String ipAddress,
        String clickedAt
) {}
