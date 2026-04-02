package org.springboot.urlshorteningservice.service;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.reposiotry.UrlRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UrlService {

    private final UrlRepo repository;
    @Value("${app.base-url}")
    private String domain;

    @Value("${app.ttl}")
    private Duration fixedDelay;

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    @CachePut(value = "URL_CACHE", key = "#result.url")
    public UrlResponse shortenUrl(CreateUrlRequest request) {

        var entity = new Url();
        entity.setUrl(request.url());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        String shortCode = generateShortcode();
        entity.setShortCode(shortCode);

        repository.save(entity);

        return new UrlResponse(
                entity.getId(),
                domain + "shorten/" + shortCode,
                shortCode,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    @Cacheable(value = "URL_CACHE", key = "#shortCode")
    public UrlStatsResponse getUrlByShortCode(String shortCode) {
        var url = findByShortCode(shortCode);
        return new UrlStatsResponse(
            url.getId(),
            url.getUrl(),
            url.getShortCode(),
            url.getCreatedAt(),
            url.getUpdatedAt()
        );
    }

    @Cacheable(value = "URL_REDIRECT", key = "#shortCode")
    public String getRedirectUrl(String shortCode) {
        var url = findByShortCode(shortCode);
        return url.getUrl();
    }

    @Caching(
        evict = {
                @CacheEvict(value = "URL_CACHE", key = "#shortCode"),
                @CacheEvict(value = "URL_REDIRECT", key = "#shortCode")
        }
    )
    public UrlResponse updateUrl(String shortCode, CreateUrlRequest urlRequest) {
        var url = findByShortCode(shortCode);

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

    @Caching(evict = {
            @CacheEvict(value = "URL_CACHE", key = "#shortCode"),
            @CacheEvict(value = "URL_REDIRECT", key = "#shortCode"),
    })
    public void removeUrl(String shortCode) {
        var url = findByShortCode(shortCode);
        repository.delete(url);
    }

    @Scheduled(fixedDelayString = "${app.ttl}")
    public void removeExpiredUrls() {
        repository.deleteByCreatedAtBefore(LocalDateTime.now().minus(fixedDelay));
    }

    private Url findByShortCode(String shortCode) {
        if(shortCode.isEmpty()){
            throw new IllegalArgumentException("Short code is empty");
        }

        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new NoSuchElementException("Short code not found"));
    }

    private String generateShortcode() {
        StringBuilder sb = new StringBuilder(7);

        for (int i = 0; i < 7; i++) {
            sb.append(BASE62.charAt(RANDOM.nextInt(BASE62.length())));
        }

        return sb.toString();
    }
}
