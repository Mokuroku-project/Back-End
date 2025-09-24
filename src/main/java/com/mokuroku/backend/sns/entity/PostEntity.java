package com.mokuroku.backend.sns.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mokuroku.backend.member.entity.Member;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class) //저장·수정 이벤트를 감시해서, 날짜/작성자 정보를 자동으로 채우는 기능
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", referencedColumnName = "email", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate // Entity가 수정될 때 자동으로 시간 정보를 업뎃해줌
    private LocalDateTime updatedDate;

    private LocalDateTime deleteDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status;

    public enum Visibility {
        PUBLIC, PRIVATE, LIMITED
    }

    /**
    게시글 삭제, 복구, 수정 같은 로직을 엔티티 내부에 적는 이유는
    필요할 때 서비스에서 이 메서드를 호출해서 처리하면 되고,
    변경 시 한 곳만 수정하면 되기 때문에
    나중에 테스트할때나 가독성 부분도 고려해서 이렇게 했습니다!
     *
      */
    // 게시물 삭제 처리
    public void delete() {
        this.status = PostStatus.DELETED;
        this.deleteDate = LocalDateTime.now();
    }

    // 게시물 복구 처리
    public void restore() {
        this.status = PostStatus.ACTIVE;
        this.deleteDate = null;
    }

    // 게시물 수정 처리
    public void update(String content, LocationEntity locationEntity, Visibility visibility) {
        this.content = content;
        this.setLocation(locationEntity);
        this.visibility = visibility;
        // updatedDate는 @LastModifiedDate로 자동 업뎃됨
    }
}