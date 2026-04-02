package org.springboot.urlshorteningservice.service;

import lombok.RequiredArgsConstructor;
import org.springboot.urlshorteningservice.dto.UrlClickedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlClickProducerService {

    public final KafkaTemplate<String, UrlClickedEvent> kafkaTemplate;

    public void sendUrlClickedEvent(UrlClickedEvent event){
        kafkaTemplate.send("url-stats", event.shortCode(), event);
    }
}
