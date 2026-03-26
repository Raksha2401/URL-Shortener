package com.urlshortener.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlResponse {
    private String shortCode;
    private String originalUrl;
    private Long clickCount;
    private LocalDateTime createdAt;
}
