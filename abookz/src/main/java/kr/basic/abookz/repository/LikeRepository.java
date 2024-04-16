package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.LikeEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
  List<LikeEntity> findAllByReview_Id(Long reviewId);
  Optional<LikeEntity> findByReview_IdAndMember_Id(Long reviewId,Long memberId);

  boolean existsByReview_IdAndMember_Id(Long reviewId, Long memberId);
}
