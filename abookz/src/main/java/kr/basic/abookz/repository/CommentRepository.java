package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
  List<CommentEntity> findAllByReview_IdAndMember_Id(Long ReviewId,Long MemberId);

  Slice<CommentEntity> findAllByReview_Id(Long reviewId, Pageable pageable);
}
