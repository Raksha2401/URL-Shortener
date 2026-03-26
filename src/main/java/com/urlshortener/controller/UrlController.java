package com.urlshortener.controller;

import com.urlshortener.model.UrlMapping;
import com.urlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlMapping> shorten(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(urlService.shortenUrl(body.get("url")));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build();
    }

    @GetMapping("/api/stats/{shortCode}")
    public ResponseEntity<UrlMapping> stats(@PathVariable String shortCode) {
        return ResponseEntity.ok(urlService.getStats(shortCode));
    }
}