package com.urlshortener.controllers;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GlobalErrorHandler extends ErrorHandler {
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int statusCode = response.getStatus();
        response.setContentType("text/plain");
        try (PrintWriter writer = response.getWriter()) {
            writer.println(getErrorMessage(statusCode));
        }
    }

    public String getErrorMessage(int statusCode) {
        return switch (statusCode) {
            case 400 -> "Bad Request. Please provide a valid Url";
            case 404 -> "The requested page was not found.";
            case 500 -> "Internal Server Error. Please try again later.";
            default -> "An unexpected error occurred.";
        };
    }
}
