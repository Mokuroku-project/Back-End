package com.mokuroku.backend.notification.event;

public record  PriceChangedEvent(
    String email,
    Long productId,
    String productName,
    int oldPrice,
    int newPrice
) {}
