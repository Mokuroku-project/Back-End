package com.mokuroku.backend.sns.dto;

import com.mokuroku.backend.member.entity.Member;
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
    public static PostDTO fromEntity(PostEntity postEntity, Member member) {
        return PostDTO.builder()
                .postId(postEntity.getPostId())
                .email(member.getEmail())
                .content(postEntity.getContent())
                .location(postEntity.getLocation())
                .visibility(postEntity.getVisibility())
                .regDate(postEntity.getRegDate())
                .updatedDate(postEntity.getUpdatedDate())
                .deleteDate(postEntity.getDeleteDate())
                .status(postEntity.getStatus())
                .build();
    }

    // DTO를 Entity로
    public PostEntity toEntity(PostDTO postDTO, Member member) {
        return PostEntity.builder()
                .postId(postDTO.getPostId())
                .member(member)
                .content(postDTO.getContent())
                .location(postDTO.getLocation())
                .visibility(postDTO.getVisibility())
                .status('1') //항상 활성 상태로
                .build();
    }

}
