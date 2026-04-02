package org.springboot.urlshorteningservice.service;

import lombok.AllArgsConstructor;
import org.springboot.urlshorteningservice.dto.UrlClickedEvent;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.model.UrlClicks;
import org.springboot.urlshorteningservice.reposiotry.UrlClickRepo;
import org.springboot.urlshorteningservice.reposiotry.UrlRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class UrlClickConsumerService {

    private final UrlClickRepo urlClicksRepo;
    private final UrlRepo urlRepo;

    @KafkaListener(topics = "url-stats", groupId = "url-click-group")
    public void consumeClickEvent(UrlClickedEvent event) {
        Url url = urlRepo.findByShortCode(event.shortCode())
                .orElseThrow(() -> new RuntimeException("URL not found: " + event.shortCode()));

        UrlClicks clicks = new UrlClicks();
        clicks.setShortCode(event.shortCode());
        clicks.setIpAddress(event.ipAddress());
        clicks.setClickedAt(LocalDateTime.ofInstant(Instant.parse(event.clickedAt()), ZoneOffset.UTC));
        clicks.setUrl(url);

        urlClicksRepo.save(clicks);
    }

}
