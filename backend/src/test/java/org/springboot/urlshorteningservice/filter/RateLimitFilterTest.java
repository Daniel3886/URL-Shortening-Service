package org.springboot.urlshorteningservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateLimitFilterTest {

    @Test
    void allowsFirstTwentyRequestsFromSameIp() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        CountingFilterChain filterChain = new CountingFilterChain();

        for (int i = 0; i < 20; i++) {
            MockHttpServletRequest request = requestFrom("203.0.113.10");
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilter(request, response, filterChain);

            assertEquals(200, response.getStatus());
        }

        assertEquals(20, filterChain.callCount);
    }

    @Test
    void rejectsTwentyFirstRequestFromSameIp() throws Exception {
        RateLimitFilter filter = new RateLimitFilter();
        CountingFilterChain filterChain = new CountingFilterChain();

        for (int i = 0; i < 20; i++) {
            filter.doFilter(requestFrom("203.0.113.10"), new MockHttpServletResponse(), filterChain);
        }

        MockHttpServletResponse blockedResponse = new MockHttpServletResponse();
        filter.doFilter(requestFrom("203.0.113.10"), blockedResponse, filterChain);

        assertEquals(429, blockedResponse.getStatus());
        assertEquals("http://localhost:3000", blockedResponse.getHeader("Access-Control-Allow-Origin"));
        assertEquals("60", blockedResponse.getHeader("Retry-After"));
        assertEquals("text/plain", blockedResponse.getContentType());
        assertEquals("Too many requests", blockedResponse.getContentAsString());
        assertEquals(20, filterChain.callCount);
    }

    private static MockHttpServletRequest requestFrom(String remoteAddress) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(remoteAddress);
        return request;
    }

    private static class CountingFilterChain implements FilterChain {
        private int callCount;

        @Override
        public void doFilter(ServletRequest request, ServletResponse response) throws IOException {
            callCount++;
        }
    }
}
