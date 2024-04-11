package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
  Optional<ReviewEntity> findByBookShelfId(Long bookShelfId);
}
