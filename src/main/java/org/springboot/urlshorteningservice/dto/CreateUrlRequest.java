package org.springboot.urlshorteningservice.dto;

import lombok.Data;

@Data
public class CreateUrlRequest {
    private String originalUrl;
}