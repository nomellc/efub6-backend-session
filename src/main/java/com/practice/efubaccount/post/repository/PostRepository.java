package com.practice.efubaccount.post.repository;

import com.practice.efubaccount.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 생성한 날짜 기준으로 정렬한 전체 조회
    List<Post> findAllByOrderByCreatedAtDesc();

    // 조회수 증가 -> 성능 상 이게 좋음. DB상에서 바로 변경되도록..
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    void increaseViewCount(@Param("postId") Long postId);
}
