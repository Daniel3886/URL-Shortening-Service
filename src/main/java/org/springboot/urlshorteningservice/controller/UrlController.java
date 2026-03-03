package org.springboot.urlshorteningservice.controller;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.CreateUrlRequest;
import org.springboot.urlshorteningservice.dto.CreateUrlResponse;
import org.springboot.urlshorteningservice.model.Url;
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
    public CreateUrlResponse shortenUrl(@RequestBody CreateUrlRequest urlRequest) {
        return urlService.shortenUrl(urlRequest);
    }

    @GetMapping("/{shortCode}/details")
    public ResponseEntity<Url> getUrlByShortCode(@PathVariable String shortCode) {
        return ResponseEntity.ok(urlService.getOriginalUrl(shortCode));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect (@PathVariable String shortCode){
        String originalUrl = urlService.getRedirectUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }


}
