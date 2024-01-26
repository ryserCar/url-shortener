package com.urlshortener.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.urlshortener.services.UrlService;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UrlController extends HttpServlet {
    private static final int SHORT_URL_LENGTH = 6;
    private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
    private final UrlService urlService;
    private final UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String shortUrl = request.getParameter("url");
        if (shortUrl == null || shortUrl.isBlank() || shortUrl.length() > SHORT_URL_LENGTH) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        String originalUrl = urlService.getOriginalUrl(shortUrl);

        if (originalUrl == null || originalUrl.isBlank()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(originalUrl);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String originalUrl = getUrlStringFromJson(request);
        String shortUrl;

        if (urlValidator.isValid(originalUrl)) {
            try {
                shortUrl = urlService.shortenUrl(originalUrl);
                logger.info("Url shortened successfully");
                response.setStatus(HttpServletResponse.SC_OK);
                shortUrl = getRedirectEndpoint(request) + shortUrl;
                sendResponse(response, shortUrl);
            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            logger.info("Provided Url does not satisfy requirements to shorten it");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private String getUrlStringFromJson(HttpServletRequest request) {
        String originalUrl = "";
        try {
            JsonObject jsonObject = JsonParser.parseReader(request.getReader()).getAsJsonObject();
            originalUrl = jsonObject.has("url") ? jsonObject.get("url").getAsString() : "";
        } catch (IllegalStateException | IOException e) {
            logger.info("Url could not be parsed from Json request");
        }
        return originalUrl;
    }

    private static String getRedirectEndpoint(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "") + "/redirect?url=";
    }

    private void sendResponse(HttpServletResponse response, String responseBody) throws IOException {
        response.setContentType("text/plain");
        try (PrintWriter writer = response.getWriter()) {
            writer.write(responseBody);
        }
    }
}
