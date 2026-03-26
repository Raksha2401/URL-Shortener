package com.urlshortener.controller;

import com.urlshortener.dto.UrlResponse;
import com.urlshortener.model.UrlMapping;
import com.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @GetMapping(value = "/")
    public ResponseEntity<String> home() {
        String html = """
                <html>
                <meta charset="UTF-8">
                <head>
                    <title>URL Shortener API</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            text-align: center;
                            margin-top: 50px;
                            background-color: #f4f4f4;
                        }
                        .container {
                            background: white;
                            padding: 30px;
                            border-radius: 10px;
                            width: 60%;
                            margin: auto;
                            box-shadow: 0 0 10px rgba(0,0,0,0.1);
                        }
                        h1 { color: #333; }
                        a {
                            display: block;
                            margin: 10px;
                            color: #007bff;
                            text-decoration: none;
                            font-weight: bold;
                        }
                        code {
                            background: #eee;
                            padding: 4px 8px;
                            border-radius: 5px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🚀 URL Shortener API</h1>
                        <p>Shorten long URLs and redirect using unique short codes</p>
                
                        <h3>📌 Endpoints</h3>
                        <p><code>POST /api/shorten</code></p>
                        <p><code>GET /{shortCode}</code></p>
                                <p><code>GET /api/stats/{shortCode}</code></p>
                
                                <div class="links">
                                            <h3>🔗 Links</h3>
                
                                            <a href="https://github.com/raksha2401/url-shortener" target="_blank">
                                                📂 View Source Code (GitHub)
                                            </a>
                
                                            <a href="https://url-shortener-production-3f34.up.railway.app/T4Ik4U" target="_blank">
                                                🚀 Live Demo (Redirect)
                                            </a>
                
                                            <a href="https://url-shortener-production-3f34.up.railway.app/api/stats/T4Ik4U" target="_blank">
                                                📊 View API Stats (JSON)
                                            </a>
                                        </div>
                            </div>
                        </body>
                        </html>
                """;

        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(html);
    }

    @PostMapping("/api/shorten")
    public ResponseEntity<UrlMapping> shorten(@RequestBody Map<String, String> body) {
        String url = body.get("url");

        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        UrlMapping result = urlService.shortenUrl(url);
        return ResponseEntity.ok(result);
    }

    // 🔁 Redirect
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build();
    }

    @GetMapping("/api/stats/{shortCode}")
    public ResponseEntity<UrlResponse> stats(@PathVariable String shortCode) {
        UrlMapping url = urlService.getStats(shortCode);

        UrlResponse res = new UrlResponse();
        res.setShortCode(url.getShortCode());
        res.setOriginalUrl(url.getOriginalUrl());
        res.setClickCount(url.getClickCount());
        res.setCreatedAt(url.getCreatedAt());

        return ResponseEntity.ok(res);
    }
}