package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
}
