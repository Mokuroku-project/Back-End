package com.mokuroku.backend.sns.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate // Entity가 수정될 떄 자동으로 시간 정보를 업뎃해줌
    private LocalDateTime updatedDate;

    private LocalDateTime deleteDate;

    @Column(nullable = false, length = 1)
    private char status;

    public enum Visibility {
        PUBLIC, PRIVATE, LIMITED
    }

    // 게시물 삭제 처리
    public void delete() {
        this.status = '0';
        this.deleteDate = LocalDateTime.now();
    }

    // 게시물 복구 처리
    public void restore() {
        this.status = '1';
        this.deleteDate = null;
    }

    // 게시물 수정 처리
    public void update(String content, String location, Visibility visibility) {
        this.content = content;
        this.location = location;
        this.visibility = visibility;
        // updatedDate는 @LastModifiedDate로 자동 업데이트됨
    }
}
