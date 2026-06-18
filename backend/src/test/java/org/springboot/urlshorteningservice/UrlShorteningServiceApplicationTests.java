package org.springboot.urlshorteningservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UrlShorteningServiceApplicationTests {

    @Test
    void applicationClassHasExpectedSpringAnnotations() {
        assertNotNull(UrlShorteningServiceApplication.class.getAnnotation(SpringBootApplication.class));
        assertNotNull(UrlShorteningServiceApplication.class.getAnnotation(EnableCaching.class));
        assertNotNull(UrlShorteningServiceApplication.class.getAnnotation(EnableScheduling.class));
    }
}
