package com.resume.hh_wrapper.impl;

import lombok.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApiClientTokenImplTest {
    private MockWebServer mockWebServer;
    private ApiClientTokenImpl apiClientToken;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Item {
        private String name;
    }

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.create(mockWebServer.url("/").toString());

        apiClientToken = new ApiClientTokenImpl(webClient);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGet_Success() {
        String responseBody = "Response from server";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE));

        String result = apiClientToken.get("/", "bearerToken", String.class);

        assertEquals(responseBody, result);
    }

    @Test
    public void testGet_WithServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(Exception.class, () -> {
            apiClientToken.get("/","bearerToken", String.class);
        });
    }

    @Test
    public void testGetList_Success() {
        String responseBody = "[{\"name\":\"Item 1\"},{\"name\":\"Item 2\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        List<Item> result = apiClientToken.getList("/", "bearerToken", Item.class);

        assertEquals(2, result.size());
        assertEquals("Item 1", result.get(0).getName());
        assertEquals("Item 2", result.get(1).getName());
    }

    @Test
    public void testGetList_WithServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(Exception.class, () -> {
            apiClientToken.getList("/","bearerToken", Item.class);
        });
    }

    @Test
    public void testPost_Success() {
        String requestBody = "example data";
        String responseBody = "Response from server";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE));

        ResponseEntity<String> result = apiClientToken.post("/", "bearerToken", requestBody, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responseBody, result.getBody());
    }

    @Test
    public void testPost_WithServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        String requestBody = "example data";

        assertThrows(Exception.class, () -> {
            apiClientToken.post("/","bearerToken", requestBody, String.class);
        });
    }

    @Test
    public void testPut_Success() {
        String requestBody = "example data";
        String responseBody = "Response from server";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE));

        String result = apiClientToken.put("/", "bearerToken", requestBody, String.class);

        assertEquals(responseBody, result);
    }

    @Test
    public void testPut_WithServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        String requestBody = "example data";

        assertThrows(Exception.class, () -> {
            apiClientToken.put("/","bearerToken", requestBody, String.class);
        });
    }
}
