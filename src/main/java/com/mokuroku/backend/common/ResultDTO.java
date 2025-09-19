package com.mokuroku.backend.common;

public record ResultDTO<T>(String message, T data) {
}
