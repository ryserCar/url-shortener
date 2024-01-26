package com.urlshortener.repository;

import com.urlshortener.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class UrlRepository implements IUrlRepository{
    private final String url;
    private final String user;
    private final String password;

    public UrlRepository() {
        Properties properties = PropertiesLoader.loadProperties();
        this.url = properties.getProperty("datasource.url");
        this.user = properties.getProperty("datasource.username");
        this.password = properties.getProperty("datasource.password");
    }

    public void saveUrl(String originalUrl, String shortUrl) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO urls (original_url, short_url) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, originalUrl);
                statement.setString(2, shortUrl);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getOriginalUrl(String shortUrl) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT original_url FROM urls WHERE short_url = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, shortUrl);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("original_url");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public String getShortUrl(String originalUrl) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT short_url FROM urls WHERE original_url = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, originalUrl);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("short_url");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }
}
