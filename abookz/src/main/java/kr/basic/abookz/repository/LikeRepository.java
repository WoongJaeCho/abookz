package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.LikeEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
  List<LikeEntity> findAllByReview_Id(Long ReviewId);
}
