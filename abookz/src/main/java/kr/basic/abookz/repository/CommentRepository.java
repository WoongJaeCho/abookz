package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
  List<CommentEntity> findAllByReview_IdAndMember_Id(Long ReviewId,Long MemberId);

}
