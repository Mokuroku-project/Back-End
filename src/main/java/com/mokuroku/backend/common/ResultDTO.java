package com.mokuroku.backend.common;

public record ResultDTO<T>(String status, T data) {

}