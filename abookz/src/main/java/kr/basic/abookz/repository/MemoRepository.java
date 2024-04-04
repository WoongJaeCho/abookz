package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<MemoEntity,Long> {
}
