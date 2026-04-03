package org.springboot.urlshorteningservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.*;
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
    @ResponseStatus(HttpStatus.CREATED)
    public UrlResponse shortenUrl(@RequestBody @Valid CreateUrlRequest urlDto) {
        return urlService.shortenUrl(urlDto);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode,
            HttpServletRequest request
    ){
        String originalUrl = urlService.handleRedirect(shortCode, request);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @PutMapping("/{shortCode}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse updateUrl(
            @PathVariable String shortCode,
            @RequestBody @Valid CreateUrlRequest urlRequest
    ) {
        return urlService.updateUrl(shortCode, urlRequest);
    }

    @DeleteMapping("/{shortCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUrl(@PathVariable String shortCode) {
        urlService.removeUrl(shortCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{shortCode}/stats")
    @ResponseStatus(HttpStatus.OK)
    public UrlStatsResponse getUrlDetails(@PathVariable String shortCode) {
        return urlService.getUrlByShortCode(shortCode);
    }

}
