package com.mokuroku.backend.bookmark.repository;

import com.mokuroku.backend.bookmark.entity.Bookmark;
import com.mokuroku.backend.member.entity.Member;
import com.mokuroku.backend.sns.entity.PostEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

  Optional<Object> findByEmailAndPostId(Member member, PostEntity post);
}
