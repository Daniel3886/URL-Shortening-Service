package org.springboot.urlshorteningservice.service;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.reposiotry.UrlRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    public UrlResponse shortenUrl(CreateUrlRequest request) {

        validateUrl(request.url());

        Url entity = new Url();
        entity.setUrl(request.url());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        String shortCode = generateShortcode();
        entity.setShortCode(shortCode);

        repository.save(entity);

        return new UrlResponse(
                entity.getId(),
                shortCode,
                domain + "shorten/" + shortCode,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
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

    public UrlResponse updateUrl(String shortCode, CreateUrlRequest urlRequest) {
        Url url = findByShortCode(shortCode);
        validateUrl(urlRequest.url());

        url.setUrl(urlRequest.url());
        url.setUpdatedAt(LocalDateTime.now());
        repository.save(url);

        return new UrlResponse (
                url.getId(),
                urlRequest.url(),
                shortCode,
                url.getCreatedAt(),
                url.getUpdatedAt()
        );
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

    private void validateUrl(String url) {
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
