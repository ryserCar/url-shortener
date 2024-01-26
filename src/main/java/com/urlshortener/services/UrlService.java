package com.urlshortener.services;

import com.urlshortener.repository.IUrlRepository;

public class UrlService {
    private final IUrlRepository urlRepository;

    int shortUrlLength;
    String upperCase;
    String lowerCase;
    String numbers;
    String charset;

    public UrlService(IUrlRepository urlRepository) {
        this.urlRepository = urlRepository;
        // As of today there are ~2 billion websites
        // If we estimate 10 links for website there are ~20 billion existing links
        // With 6 characters for a short url we can map ~56.5 billion links (62 chars between lower/uppercase and numbers)
        this.shortUrlLength = 6;
        this.upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        this.lowerCase = upperCase.toLowerCase();
        this.numbers = "0123456789";
        this.charset = upperCase + lowerCase + numbers;
    }

    public String shortenUrl(String originalUrl) {
        String shortKey = urlRepository.getShortUrl(originalUrl);
        if (shortKey.isBlank()) {
            do {
                shortKey = generateRandomString();
            } while (!urlRepository.getOriginalUrl(shortKey).isBlank());
            urlRepository.saveUrl(originalUrl, shortKey);
        }
        return shortKey;
    }

    public String getOriginalUrl(String shortKey) {
        return urlRepository.getOriginalUrl(shortKey);
    }


    private String generateRandomString() {
        StringBuilder randomString = new StringBuilder(shortUrlLength);
        for (int i = 0; i < shortUrlLength; i++) {
            randomString.append(charset.charAt((int) (Math.random() * charset.length())));
        }
        return randomString.toString();
    }
}
