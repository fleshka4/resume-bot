package com.resume.unit.hh_wrapper.impl;

import com.resume.hh_wrapper.impl.ApiClientImpl;
import lombok.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ApiClientImplTest {
    private MockWebServer mockWebServer;
    private ApiClientImpl apiClient;

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

        WebClient webClient =  WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();

        apiClient = new ApiClientImpl(webClient);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testGet_Success() {
        String responseBody = "Mocked Response";
        mockWebServer.enqueue(new MockResponse().setBody(responseBody));

        String result = apiClient.get("/", String.class);

        assertEquals(responseBody, result);
    }

    @Test
    public void testGet_WithServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(Exception.class, () -> {
            apiClient.get("/", String.class);
        });
    }

    @Test
    public void testGetList_Success() {
        String responseBody = "[{\"name\":\"Item 1\"},{\"name\":\"Item 2\"}]";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        List<Item> result = apiClient.getList("/", Item.class);

        assertEquals(2, result.size());
        assertEquals("Item 1", result.get(0).getName());
        assertEquals("Item 2", result.get(1).getName());
    }

    @Test
    public void testGetList_WithServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        assertThrows(Exception.class, () -> {
            apiClient.getList("/", Item.class);
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

        String result = apiClient.post("/", requestBody, String.class);

        assertEquals(responseBody, result);
    }

    @Test
    public void testPut_Success() {
        String requestBody = "example data";
        String responseBody = "Response from server";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE));

        String result = apiClient.put("/", requestBody, String.class);

        assertEquals(responseBody, result);
    }
}
