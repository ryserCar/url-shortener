package com.urlshortener.services;

import com.urlshortener.repository.IUrlRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UrlServiceTest {
    IUrlRepository urlRepository = mock(IUrlRepository.class);
    UrlService urlService = new UrlService(urlRepository);

    @BeforeEach
    public void setUp() {
        when(urlRepository.getShortUrl(anyString())).thenReturn("AfdS24");
        when(urlRepository.getOriginalUrl(anyString())).thenReturn("https://www.google.com");
    }

    @Test
    public void shortenUrl_shouldShortenAValidURL() {
        String originalUrl = "https://www.google.com";
        String shortKey = urlService.shortenUrl(originalUrl);
        Assertions.assertEquals(6, shortKey.length());
        Assertions.assertFalse(urlService.getOriginalUrl(shortKey).isBlank());
    }

    @Test
    public void shortenUrl_shouldGenerateANewShortKeyForAnExistingURL() {
        String originalUrl = "https://www.google.com";
        String shortKey1 = urlService.shortenUrl(originalUrl);
        String shortKey2 = urlService.shortenUrl(originalUrl);
        Assertions.assertEquals(shortKey1, shortKey2);
    }

    @Test
    public void getOriginalUrl_shouldReturnTheOriginalUrlForValidShortKey() {
        String originalUrl = "https://www.google.com";
        String shortKey = urlService.shortenUrl(originalUrl);
        String retrievedUrl = urlService.getOriginalUrl(shortKey);
        Assertions.assertEquals(originalUrl, retrievedUrl);
    }

    @Test
    public void getOriginalUrl_shouldReturnAnEmptyStringForInvalidShortKey() {
        when(urlRepository.getOriginalUrl(anyString())).thenReturn("");
        String shortKey = "invalid-short-key";

        String retrievedUrl = urlService.getOriginalUrl(shortKey);

        Assertions.assertTrue(retrievedUrl.isBlank());
    }

    @Test
    public void shortenUrl_shouldAttemptToGetAnotherShortURLWhenIsAlreadyUsed() {
        //first attempt to create a short url fails
        when(urlRepository.getOriginalUrl("s1238A")).thenReturn("https://www.google.com");
        //second attempt to create a short url works
        when(urlRepository.getOriginalUrl(anyString())).thenReturn("");
        when(urlRepository.getShortUrl(anyString())).thenReturn("");

        UrlService urlService = new UrlService(urlRepository);

        String shortKey = urlService.shortenUrl("https://mywebsite.com");

        Assertions.assertNotNull(shortKey);
    }
}