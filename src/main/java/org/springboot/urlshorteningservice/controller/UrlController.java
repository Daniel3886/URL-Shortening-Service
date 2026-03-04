package org.springboot.urlshorteningservice.controller;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.UrlRequest;
import org.springboot.urlshorteningservice.dto.ShortCodeRequest;
import org.springboot.urlshorteningservice.dto.UrlResponse;
import org.springboot.urlshorteningservice.dto.UrlStatsResponse;
import org.springboot.urlshorteningservice.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/shorten")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    public UrlResponse shortenUrl(@RequestBody UrlRequest urlRequest) {
        return urlService.shortenUrl(urlRequest);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable ShortCodeRequest shortCode){
        String originalUrl = urlService.getRedirectUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @PutMapping("/{shortCode}")
    public UrlResponse updateUrl(
            @PathVariable ShortCodeRequest shortCode,
            @RequestBody UrlRequest urlRequest
    ) {
        return urlService.updateUrl(shortCode, urlRequest);
    }

    @GetMapping("/{shortCode}/details")
    public UrlStatsResponse getUrlDetails(@PathVariable ShortCodeRequest shortCode) {
        return urlService.getUrlByShortCode(shortCode);
    }

}
