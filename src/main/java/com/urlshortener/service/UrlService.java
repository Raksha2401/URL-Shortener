package com.urlshortener.service;

import com.urlshortener.model.UrlMapping;
import com.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;

    private static final String BASE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public UrlMapping shortenUrl(String originalUrl) {
        String shortCode = generateCode();
        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);
        mapping.setOriginalUrl(originalUrl);
        mapping.setClickCount(0L);
        mapping.setCreatedAt(LocalDateTime.now());
        return urlRepository.save(mapping);
    }

    public String getOriginalUrl(String shortCode) {
        UrlMapping mapping = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));
        mapping.setClickCount(mapping.getClickCount() + 1);
        urlRepository.save(mapping);
        return mapping.getOriginalUrl();
    }

    public UrlMapping getStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found"));
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++)
            sb.append(BASE_CHARS.charAt(random.nextInt(BASE_CHARS.length())));
        return sb.toString();
    }
}
