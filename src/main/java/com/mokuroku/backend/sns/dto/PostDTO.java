package com.mokuroku.backend.sns.dto;

import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.sns.entity.LocationEntity;
import com.mokuroku.backend.sns.entity.PostEntity;
import com.mokuroku.backend.sns.entity.PostStatus;
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
    private Double latitude;
    private Double longitude;
    private PostEntity.Visibility visibility;
    private LocalDateTime regDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deleteDate;
    private PostStatus status;

    // Entity를 DTO로
    public static PostDTO fromEntity(PostEntity postEntity, Member member) {
        LocationEntity locationEntity = postEntity.getLocation();

        return PostDTO.builder()
                .postId(postEntity.getPostId())
                .email(member.getEmail())
                .content(postEntity.getContent())
                .latitude(locationEntity.getLatitude())
                .longitude(locationEntity.getLongitude())
                .visibility(postEntity.getVisibility())
                .regDate(postEntity.getRegDate())
                .updatedDate(postEntity.getUpdatedDate())
                .deleteDate(postEntity.getDeleteDate())
                .status(postEntity.getStatus())
                .build();
    }

    // DTO를 Entity로
    public PostEntity toEntity(PostDTO postDTO, Member member,
                               LocationEntity locationEntity) {
        return PostEntity.builder()
                .postId(postDTO.getPostId())
                .member(member)
                .content(postDTO.getContent())
                .location(locationEntity)
                .visibility(postDTO.getVisibility())
                .status(postDTO.getStatus())
                .build();
    }

}