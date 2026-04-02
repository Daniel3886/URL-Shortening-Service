package org.springboot.urlshorteningservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springboot.urlshorteningservice.dto.UrlClickedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;


    @Bean
    public ConsumerFactory<String, UrlClickedEvent> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "url-stats-consumer");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UrlClickedEvent.class);
        configProps.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "org.springboot.urlshorteningservice.dto");

        return new DefaultKafkaConsumerFactory<>(
                configProps,
                new StringDeserializer(),
                new JacksonJsonDeserializer<>(UrlClickedEvent.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UrlClickedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UrlClickedEvent> factory
                = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
