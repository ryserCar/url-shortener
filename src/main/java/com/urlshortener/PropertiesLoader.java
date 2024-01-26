package com.urlshortener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = UrlShortenerApplication.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Unable to find application.properties file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
