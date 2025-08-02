package com.mokuroku.backend.common;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}