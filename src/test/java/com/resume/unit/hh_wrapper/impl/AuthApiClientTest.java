package com.resume.unit.hh_wrapper.impl;

import com.resume.hh_wrapper.impl.AuthApiClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthApiClientTest {

    private MockWebServer mockWebServer;
    private AuthApiClient authApiClient;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClient webClient = WebClient.create(mockWebServer.url("/").toString());

        authApiClient = new AuthApiClient(webClient);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testAuth_Success() {
        String requestBody = "example data";
        String responseBody = "Response from server";
        mockWebServer.enqueue(new MockResponse()
                .setBody(responseBody)
                .setResponseCode(HttpStatus.OK.value())
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE));

        String result = authApiClient.auth("/", requestBody);

        assertEquals(responseBody, result);
    }

    @Test
    public void testGet_WithServerError() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        String requestBody = "example data";

        assertThrows(Exception.class, () -> {
            authApiClient.auth("/",requestBody);
        });
    }
}
