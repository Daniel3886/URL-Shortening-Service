package org.springboot.urlshorteningservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springboot.urlshorteningservice.dto.UrlRequest;
import org.springboot.urlshorteningservice.dto.UrlResponse;
import org.springboot.urlshorteningservice.dto.UrlStatsResponse;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.reposiotry.UrlRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UrlService {

    private final UrlRepo repository;
    @Value("${app.base-url}")
    private String domain;
    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    public UrlResponse shortenUrl(UrlRequest request) throws InvalidUrlException {

        validateUrl(request.getUrl());

        Url entity = new Url();
        entity.setUrl(request.getUrl());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        String shortCode = generateShortcode();
        entity.setShortCode(shortCode);

        repository.save(entity);

        return UrlResponse.builder()
                .id(entity.getId())
                .url(domain + "shorten/" + shortCode)
                .shortCode(shortCode)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public UrlStatsResponse getUrlByShortCode(String shortCode) {
        Url url = findByShortCode(shortCode);
        return new UrlStatsResponse(
            url.getId(),
            url.getUrl(),
            url.getShortCode(),
            url.getCreatedAt(),
            url.getUpdatedAt()
        );
    }

    public String getRedirectUrl(String shortCode) {
        Url url = findByShortCode(shortCode);
        return url.getUrl();
    }

    public UrlResponse updateUrl(String shortCode, UrlRequest urlRequest) {
        Url url = findByShortCode(shortCode);
        validateUrl(urlRequest.getUrl());

        url.setUrl(urlRequest.getUrl());
        url.setUpdatedAt(LocalDateTime.now());
        repository.save(url);

        return UrlResponse.builder()
                .id(url.getId())
                .url(urlRequest.getUrl())
                .shortCode(shortCode)
                .createdAt(url.getCreatedAt())
                .updatedAt(url.getUpdatedAt())
                .build();
    }

    public void removeUrl(String shortCode) {
        Url url = findByShortCode(shortCode);
        repository.delete(url);
    }

    @Scheduled(fixedDelay = 24, timeUnit = TimeUnit.HOURS)
    public void removeExpiredUrls() {
        repository.deleteByCreatedAtBefore(LocalDateTime.now().minusDays(1));
    }

    private Url findByShortCode(String shortCode) {
        if(shortCode.isEmpty()){
            throw new IllegalArgumentException("Short code is empty");
        }

        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new NoSuchElementException("Short code not found"));
    }

    private void validateUrl(String url) throws InvalidUrlException {
        try {
            if(url.isEmpty()){
                throw new IllegalArgumentException("URL is empty");
            }

            URI uri = new URI(url);
            if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
                throw new IllegalArgumentException("URL must start with http or https");
            }
        } catch (URISyntaxException e){
            throw new IllegalArgumentException("Invalid URL format: " + e.getMessage());
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
