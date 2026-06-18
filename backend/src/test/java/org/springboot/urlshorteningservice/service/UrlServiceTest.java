package org.springboot.urlshorteningservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springboot.urlshorteningservice.dto.CreateUrlRequest;
import org.springboot.urlshorteningservice.dto.UrlClickedEvent;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.repository.UrlRepo;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Proxy;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UrlServiceTest {

    private FakeUrlRepo repository;
    private CapturingProducerService producerService;
    private UrlService service;

    @BeforeEach
    void setUp() {
        repository = new FakeUrlRepo();
        producerService = new CapturingProducerService();
        service = newService(repository, producerService);
    }

    @Test
    void shortenUrlPersistsUrlAndReturnsShortUrlResponse() {
        var response = service.shortenUrl(new CreateUrlRequest("https://example.com/articles/1"));

        Url savedUrl = repository.savedUrl;
        assertEquals("https://example.com/articles/1", savedUrl.getUrl());
        assertEquals(7, savedUrl.getShortCode().length());
        assertNotNull(savedUrl.getCreatedAt());
        assertNotNull(savedUrl.getUpdatedAt());

        assertEquals(savedUrl.getId(), response.id());
        assertEquals(savedUrl.getShortCode(), response.shortCode());
        assertEquals("https://sho.rt/shorten/" + savedUrl.getShortCode(), response.url());
        assertEquals(savedUrl.getCreatedAt(), response.createdAt());
        assertEquals(savedUrl.getUpdatedAt(), response.updatedAt());
    }

    @Test
    void getUrlByShortCodeReturnsStatsForExistingShortCode() {
        Url url = url("abc1234", "https://example.com");
        repository.urlsByShortCode.put("abc1234", url);

        var response = service.getUrlByShortCode("abc1234");

        assertEquals(url.getId(), response.id());
        assertEquals("https://example.com", response.url());
        assertEquals("abc1234", response.shortCode());
        assertEquals(url.getCreatedAt(), response.createdAt());
        assertEquals(url.getUpdatedAt(), response.updatedAt());
    }

    @Test
    void getRedirectUrlThrowsWhenShortCodeIsMissing() {
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> service.getRedirectUrl("missing")
        );

        assertEquals("Short code not found", exception.getMessage());
    }

    @Test
    void getRedirectUrlRejectsEmptyShortCode() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.getRedirectUrl("")
        );

        assertEquals("Short code is empty", exception.getMessage());
    }

    @Test
    void handleRedirectReturnsOriginalUrlAndPublishesClickEvent() {
        repository.urlsByShortCode.put("abc1234", url("abc1234", "https://example.com"));
        HttpServletRequest request = requestFrom("203.0.113.10");

        String redirectUrl = service.handleRedirect("abc1234", request);

        assertEquals("https://example.com", redirectUrl);

        UrlClickedEvent event = producerService.event;
        assertEquals("abc1234", event.shortCode());
        assertEquals("203.0.113.10", event.ipAddress());
        assertFalse(event.clickedAt().isBlank());
    }

    @Test
    void updateUrlChangesOriginalUrlAndUpdatedAt() {
        Url existing = url("abc1234", "https://old.example.com");
        LocalDateTime originalUpdatedAt = existing.getUpdatedAt();
        repository.urlsByShortCode.put("abc1234", existing);

        var response = service.updateUrl("abc1234", new CreateUrlRequest("https://new.example.com"));

        assertSame(existing, repository.savedUrl);
        assertEquals("https://new.example.com", existing.getUrl());
        assertTrue(existing.getUpdatedAt().isAfter(originalUpdatedAt));
        assertEquals(existing.getId(), response.id());
        assertEquals("https://new.example.com", response.url());
        assertEquals("abc1234", response.shortCode());
    }

    @Test
    void removeExpiredUrlsDeletesUrlsOlderThanConfiguredTtl() {
        service.removeExpiredUrls();

        assertTrue(repository.deletedBefore.isBefore(LocalDateTime.now().minusDays(29)));
        assertTrue(repository.deletedBefore.isAfter(LocalDateTime.now().minusDays(31)));
    }

    private static Url url(String shortCode, String originalUrl) {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now().minusHours(1);
        Url url = new Url();
        url.setId(UUID.randomUUID());
        url.setShortCode(shortCode);
        url.setUrl(originalUrl);
        url.setCreatedAt(createdAt);
        url.setUpdatedAt(updatedAt);
        return url;
    }

    private static HttpServletRequest requestFrom(String remoteAddress) {
        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getRemoteAddr")) {
                        return remoteAddress;
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }

    private static class CapturingProducerService extends UrlClickProducerService {
        private UrlClickedEvent event;

        CapturingProducerService() {
            super(null);
        }

        @Override
        public void sendUrlClickedEvent(UrlClickedEvent event) {
            this.event = event;
        }
    }

    private static class FakeUrlRepo implements java.lang.reflect.InvocationHandler {
        private final Map<String, Url> urlsByShortCode = new HashMap<>();
        private Url savedUrl;
        private LocalDateTime deletedBefore;

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            return switch (method.getName()) {
                case "save" -> save(assertInstanceOf(Url.class, args[0]));
                case "findByShortCode" -> Optional.ofNullable(urlsByShortCode.get((String) args[0]));
                case "deleteByCreatedAtBefore" -> {
                    deletedBefore = (LocalDateTime) args[0];
                    yield null;
                }
                case "delete" -> {
                    urlsByShortCode.remove(assertInstanceOf(Url.class, args[0]).getShortCode());
                    yield null;
                }
                default -> throw new UnsupportedOperationException(method.getName());
            };
        }

        private Url save(Url url) {
            if (url.getId() == null) {
                url.setId(UUID.randomUUID());
            }
            savedUrl = url;
            urlsByShortCode.put(url.getShortCode(), url);
            return url;
        }
    }

    private static UrlRepo newRepo(FakeUrlRepo fake) {
        return (UrlRepo) Proxy.newProxyInstance(
                UrlRepo.class.getClassLoader(),
                new Class<?>[]{UrlRepo.class},
                fake
        );
    }

    private static UrlService newService(FakeUrlRepo fake, CapturingProducerService producer) {
        UrlService urlService = new UrlService(newRepo(fake), producer);
        ReflectionTestUtils.setField(urlService, "domain", "https://sho.rt/");
        ReflectionTestUtils.setField(urlService, "fixedDelay", Duration.ofDays(30));
        ReflectionTestUtils.setField(urlService, "self", urlService);
        return urlService;
    }
}
