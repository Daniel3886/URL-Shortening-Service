package org.springboot.urlshorteningservice.controller;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.UrlRequest;
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UrlResponse> shortenUrl(@RequestBody UrlRequest urlRequest) {

        UrlResponse response =  urlService.shortenUrl(urlRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode){
        String originalUrl = urlService.getRedirectUrl(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @PutMapping("/{shortCode}")
    @ResponseStatus(HttpStatus.OK)
    public UrlResponse updateUrl(
            @PathVariable String shortCode,
            @RequestBody UrlRequest urlRequest
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
