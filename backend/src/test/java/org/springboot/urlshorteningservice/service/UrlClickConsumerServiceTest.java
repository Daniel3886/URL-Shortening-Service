package org.springboot.urlshorteningservice.service;

import org.junit.jupiter.api.Test;
import org.springboot.urlshorteningservice.dto.UrlClickedEvent;
import org.springboot.urlshorteningservice.model.Url;
import org.springboot.urlshorteningservice.model.UrlClicks;
import org.springboot.urlshorteningservice.repository.UrlClickRepo;
import org.springboot.urlshorteningservice.repository.UrlRepo;

import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlClickConsumerServiceTest {

    @Test
    void consumeClickEventPersistsClickForExistingUrl() {
        FakeUrlRepo urlRepo = new FakeUrlRepo();
        FakeUrlClickRepo urlClicksRepo = new FakeUrlClickRepo();
        Url url = new Url();
        url.setShortCode("abc1234");
        urlRepo.urlsByShortCode.put("abc1234", url);

        UrlClickConsumerService service = new UrlClickConsumerService(newUrlClickRepo(urlClicksRepo), newUrlRepo(urlRepo));
        service.consumeClickEvent(new UrlClickedEvent(
                "abc1234",
                "203.0.113.10",
                "2026-06-18T10:15:30Z"
        ));

        UrlClicks click = urlClicksRepo.savedClick;
        assertEquals("abc1234", click.getShortCode());
        assertEquals("203.0.113.10", click.getIpAddress());
        assertEquals(LocalDateTime.of(2026, 6, 18, 10, 15, 30), click.getClickedAt());
        assertSame(url, click.getUrl());
    }

    @Test
    void consumeClickEventThrowsWhenUrlDoesNotExist() {
        FakeUrlRepo urlRepo = new FakeUrlRepo();
        FakeUrlClickRepo urlClicksRepo = new FakeUrlClickRepo();
        UrlClickConsumerService service = new UrlClickConsumerService(newUrlClickRepo(urlClicksRepo), newUrlRepo(urlRepo));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.consumeClickEvent(new UrlClickedEvent(
                "missing",
                "203.0.113.10",
                "2026-06-18T10:15:30Z"
        )));

        assertEquals("URL not found: missing", exception.getMessage());
    }

    private static UrlRepo newUrlRepo(FakeUrlRepo fake) {
        return (UrlRepo) Proxy.newProxyInstance(
                UrlRepo.class.getClassLoader(),
                new Class<?>[]{UrlRepo.class},
                fake
        );
    }

    private static UrlClickRepo newUrlClickRepo(FakeUrlClickRepo fake) {
        return (UrlClickRepo) Proxy.newProxyInstance(
                UrlClickRepo.class.getClassLoader(),
                new Class<?>[]{UrlClickRepo.class},
                fake
        );
    }

    private static class FakeUrlRepo implements java.lang.reflect.InvocationHandler {
        private final Map<String, Url> urlsByShortCode = new HashMap<>();

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if (method.getName().equals("findByShortCode")) {
                return Optional.ofNullable(urlsByShortCode.get((String) args[0]));
            }
            throw new UnsupportedOperationException(method.getName());
        }
    }

    private static class FakeUrlClickRepo implements java.lang.reflect.InvocationHandler {
        private UrlClicks savedClick;

        @Override
        public Object invoke(Object proxy, java.lang.reflect.Method method, Object[] args) {
            if (method.getName().equals("save")) {
                savedClick = assertInstanceOf(UrlClicks.class, args[0]);
                return savedClick;
            }
            throw new UnsupportedOperationException(method.getName());
        }
    }
}
