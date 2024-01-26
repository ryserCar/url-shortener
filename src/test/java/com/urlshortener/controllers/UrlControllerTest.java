package com.urlshortener.controllers;

import com.urlshortener.services.UrlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UrlControllerTest {
    private static final String VALID_ORIGINAL_URL = "https://mywebsite.com";
    private static final String VALID_SHORT_URL = "Vas24A";

    private UrlService urlService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UrlController urlController;

    @BeforeEach
    void setUp() throws IOException {
        urlService = Mockito.mock(UrlService.class);
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        urlController = new UrlController(urlService);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        when(request.getParameter("url")).thenReturn(VALID_ORIGINAL_URL);

    }

    @Test
    void doGet_shouldRedirectToOriginalUrlForValidShortUrl() throws Exception {
        when(request.getParameter("url")).thenReturn(VALID_SHORT_URL);
        when(urlService.getOriginalUrl(VALID_SHORT_URL)).thenReturn(VALID_ORIGINAL_URL);

        urlController.doGet(request, response);

        verify(response, times(0)).sendError(anyInt(), anyString());
        verify(response, times(1)).sendRedirect(VALID_ORIGINAL_URL);
    }

    @Test
    void doGet_shouldFailWithNullShortUrl() throws Exception {
        when(request.getParameter("url")).thenReturn(null);

        urlController.doGet(request, response);
        verify(response, times(1)).sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost_shouldReturnValidShortUrl() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"url\":\"" + VALID_ORIGINAL_URL + "\"}")));
        when(request.getRequestURI()).thenReturn("shortenUrl");
        when(request.getRequestURL()).thenReturn(new StringBuffer("localhost:8080/shortenUrl"));

        when(urlService.shortenUrl(VALID_ORIGINAL_URL)).thenReturn(VALID_SHORT_URL);

        urlController.doPost(request, response);

//        Assertions.assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);

    }

    @Test
    void doPost_shouldFailForInvalidJsonRequest() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("invalidJson")));

        urlController.doPost(request, response);

        verify(response, times(1)).sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

}