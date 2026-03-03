package org.springboot.urlshorteningservice.service;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.CreateUrlRequest;
import org.springboot.urlshorteningservice.dto.CreateUrlResponse;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.reposiotry.UrlRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UrlService {

    private final UrlRepo repository;
    // TODO Use in application.yml and dont hardcode the variable
    private final String domain = "http://localhost:8080/";

    private static final String BASE62 =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final SecureRandom RANDOM = new SecureRandom();

    public CreateUrlResponse shortenUrl(CreateUrlRequest request) throws InvalidUrlException {

        validateUrl(request.getOriginalUrl());

        Url entity = new Url();
        entity.setOriginalUrl(request.getOriginalUrl());
        entity.setCreatedAt(LocalDateTime.now());
        String shortCode = generateShortcode();
        entity.setShortCode(shortCode);

        repository.save(entity);

        return new CreateUrlResponse(
                shortCode,
                domain + "shorten/" +  shortCode,
                entity.getCreatedAt()
        );
    }

    public Url getOriginalUrl(String shortCode) {

        if(shortCode == null || shortCode.isEmpty()) {
            throw new IllegalArgumentException("Short code cannot be empty");
        }

        return repository.findByShortCode(shortCode)
                .orElseThrow(() -> new NoSuchElementException("No URL found for shortCode: " + shortCode));
    }

    public String getRedirectUrl(String shortCode) {
        String originalUrl = getOriginalUrl(shortCode).getOriginalUrl();

        if (originalUrl == null || originalUrl.isBlank()) {
            throw new InvalidUrlException("Original URL is empty");
        }

        validateUrl(originalUrl);

        return originalUrl;
    }

    private void validateUrl(String url) throws InvalidUrlException {
        try {
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
