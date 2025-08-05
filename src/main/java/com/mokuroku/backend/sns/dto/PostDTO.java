package com.mokuroku.backend.sns.dto;

import com.mokuroku.backend.sns.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private Long postId;
    private String email;
    private String content;
    private String location;
    private PostEntity.Visibility visibility;
    private LocalDateTime regDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deleteDate;
    private char status;

    // Entity를 DTO로
    public static PostDTO fromEntity(PostEntity entity) {
        return PostDTO.builder()
                .postId(entity.getPostId())
                .email(entity.getEmail())
                .content(entity.getContent())
                .location(entity.getLocation())
                .visibility(entity.getVisibility())
                .regDate(entity.getRegDate())
                .updatedDate(entity.getUpdatedDate())
                .deleteDate(entity.getDeleteDate())
                .status(entity.getStatus())
                .build();
    }

    // DTO를 Entity로
    public PostEntity toEntity(PostDTO postDTO) {
        return PostEntity.builder()
                .postId(postDTO.getPostId())
                .email(postDTO.getEmail())
                .content(postDTO.getContent())
                .location(postDTO.getLocation())
                .visibility(postDTO.getVisibility())
                .status(postDTO.getStatus())
                .build();
    }
}
