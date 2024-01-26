package com.urlshortener.controllers;

import org.eclipse.jetty.server.Request;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GlobalErrorHandlerTest {

    @Test
    void handle_ShouldHandleErrorAndWriteMessage() throws IOException {
        Request baseRequest = mock(Request.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        GlobalErrorHandler globalErrorHandler = new GlobalErrorHandler();

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        when(response.getStatus()).thenReturn(HttpServletResponse.SC_BAD_REQUEST);

        globalErrorHandler.handle("target", baseRequest, request, response);

        verify(response).setContentType("text/plain");

        String expectedErrorMessage = globalErrorHandler.getErrorMessage(response.getStatus());

        assertEquals(expectedErrorMessage, stringWriter.toString().trim());
    }
}

