package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
}
