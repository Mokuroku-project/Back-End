package com.mokuroku.backend.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "API 응답 DTO")
public class ResultDTO<T> {
    @Schema(description = "응답 상태", example = "success")
    private String status;

    @Schema(description = "응답 데이터", nullable = true)
    private T data;
}