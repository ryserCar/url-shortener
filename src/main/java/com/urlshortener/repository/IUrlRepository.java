package com.urlshortener.repository;

public interface IUrlRepository {
    void saveUrl(String originalUrl, String shortUrl);

    String getOriginalUrl(String shortUrl);

    String getShortUrl(String originalUrl);
}
