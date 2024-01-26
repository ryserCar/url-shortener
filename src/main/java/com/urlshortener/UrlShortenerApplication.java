package com.urlshortener;

import com.urlshortener.controllers.GlobalErrorHandler;
import com.urlshortener.controllers.UrlController;
import com.urlshortener.repository.UrlRepository;
import com.urlshortener.services.UrlService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlShortenerApplication {
    private static final Logger logger = LoggerFactory.getLogger(UrlShortenerApplication.class);

    public static void main(String[] args) throws Exception {
        UrlRepository urlRepository = new UrlRepository();
        UrlService urlService = new UrlService(urlRepository);
        UrlController urlController = new UrlController(urlService);

        Server server = new Server(8080);

        GlobalErrorHandler errorHandler = new GlobalErrorHandler();
        server.setErrorHandler(errorHandler);

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("/");
        contextHandler.addServlet(new ServletHolder(urlController), "/shortenUrl");
        contextHandler.addServlet(new ServletHolder(urlController), "/redirect/*");
        server.setHandler(contextHandler);

        server.start();
        logger.info("Server is up and running on port 8080");
    }
}
