package com.jachaks.redisshortener.service;

import com.jachaks.redisshortener.util.Base62;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class UrlShortenerService {

    private static final String URL_PREFIX = "http://smol.link/";

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String shortenUrl(String originalUrl) {
        String id = generateShortId(originalUrl);
        redisTemplate.opsForValue().set(id, originalUrl, 7, TimeUnit.DAYS);
        String shortUrl = URL_PREFIX + id;
        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        String id = shortUrl.replace(URL_PREFIX, "");
        String originalUrl = redisTemplate.opsForValue().get(id);
        return originalUrl;
    }

    private String generateShortId(String originalUrl) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(originalUrl.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            return Base62.encode(number).substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash for URL", e);
        }
    }
}
