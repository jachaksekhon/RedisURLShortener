package com.jachaks.redisshortener.controller;

import com.jachaks.redisshortener.service.UrlShortenerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/url")
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService urlShortenerService;

    @PostMapping("/shorten")
    public String shortenUrl(@RequestBody String originalUrl) {
        return urlShortenerService.shortenUrl(originalUrl);
    }

    @GetMapping("/original")
    public String getOriginalUrl(@RequestParam(name = "shortUrl") String shortUrl) {
        return urlShortenerService.getOriginalUrl(shortUrl);
    }

    @GetMapping("/{id}")
    public void redirect(@PathVariable String id, HttpServletResponse response) {
        String shortUrl = "http://smol.link/" + id;
        String originalUrl = urlShortenerService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            try {
                response.sendRedirect(originalUrl);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during redirection", e);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found");
        }
    }
}
