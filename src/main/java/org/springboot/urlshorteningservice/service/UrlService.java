package org.springboot.urlshorteningservice.service;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.CreateUrlRequest;
import org.springboot.urlshorteningservice.dto.ShortCodeRequest;
import org.springboot.urlshorteningservice.dto.UrlResponse;
import org.springboot.urlshorteningservice.dto.UrlStatsResponse;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.reposiotry.UrlRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UrlService {

    private final UrlRepo repository;
    // TODO Use in application.yml and dont hardcode the variable
    private final String domain = "http://localhost:8080/";

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    public UrlResponse shortenUrl(CreateUrlRequest request) throws InvalidUrlException {

        validateUrl(request.getUrl());

        Url entity = new Url();
        entity.setUrl(request.getUrl());
        entity.setCreatedAt(LocalDateTime.now());
        String shortCode = generateShortcode();
        entity.setShortCode(shortCode);

        repository.save(entity);

        return UrlResponse.builder()
                .id(entity.getId())
                .url(domain + "shorten/" + shortCode) // TODO Figure out if this shorten/url is needed or can be removed
                .shortCode(shortCode)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UrlStatsResponse getUrlByShortCode(ShortCodeRequest shortCode) {
        if(shortCode.getShortCode().isEmpty()){
            throw new IllegalArgumentException("Short code is empty");
        }

        Url url = repository.findByShortCode(shortCode.getShortCode())
                .orElseThrow(() -> new IllegalArgumentException("Short code not found"));

        return new UrlStatsResponse(
            url.getId(),
            url.getUrl(),
            url.getShortCode(),
            url.getCreatedAt(),
            url.getUpdatedAt()
        );
    }

    public String getRedirectUrl(ShortCodeRequest shortCode) {
        if(shortCode.getShortCode().isEmpty()){
            throw new IllegalArgumentException("Short code is empty");
        }

        Url original = repository.findByShortCode(shortCode.getShortCode())
                .orElseThrow(() -> new IllegalArgumentException("Short code not found"));

        return original.getUrl();
    }

    private void validateUrl(String url) throws InvalidUrlException {
        try {
            if(url.isEmpty()){
                throw new InvalidUrlException("URL is empty");
            }

            URI uri = new URI(url);
            if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
                throw new InvalidUrlException("URL must start with http or https");
            }
        } catch (URISyntaxException e){
            throw new InvalidUrlException("Invalid URL format: " + e.getMessage());
        }
    }

    private String generateShortcode() {
        StringBuilder sb = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {
            sb.append(BASE62.charAt(RANDOM.nextInt(BASE62.length())));
        }

        return sb.toString();
    }
}
