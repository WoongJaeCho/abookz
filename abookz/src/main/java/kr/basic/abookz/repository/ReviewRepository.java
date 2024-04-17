package kr.basic.abookz.repository;

import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Long> {
  Optional<ReviewEntity> findByBookShelfId(Long bookShelfId);

  @Query("SELECT r FROM ReviewEntity r WHERE r.bookShelf.book.ISBN13 = :ISBN13")
  Page<ReviewEntity> findByBookISBN13(@Param("ISBN13") String ISBN13, Pageable pageable);
}
