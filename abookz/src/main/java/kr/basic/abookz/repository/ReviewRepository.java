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

  @Query("SELECT r FROM ReviewEntity r WHERE r.bookShelf.book.ISBN13 = :ISBN13 AND r.content LIKE %:content%")
  Page<ReviewEntity> findByBookISBN13AndContent(@Param("ISBN13") String ISBN13, @Param("content") String content, Pageable pageable);

  @Query(value = "SELECT r.*, bs.BOOKSHELF_ID AS bs_bookshelf_id, b.*, likes.likeCount FROM REVIEW r " +
      "INNER JOIN BOOKSHELF bs ON r.BOOKSHELF_ID = bs.BOOKSHELF_ID " +
      "INNER JOIN BOOK b ON bs.BOOK_ID = b.BOOK_ID " +
      "LEFT JOIN (SELECT l.REVIEW_ID, COUNT(l.LIKE_ID) AS likeCount FROM REVIEW_LIKE l GROUP BY l.REVIEW_ID) likes ON r.REVIEW_ID = likes.REVIEW_ID " +
      "WHERE b.ISBN13 = :ISBN13 AND r.CONTENT LIKE CONCAT('%', :content, '%') " +
      "ORDER BY likes.likeCount DESC", nativeQuery = true)
  Page<ReviewEntity> findByBookISBN13AndContentSortedByLikes(@Param("ISBN13") String ISBN13, @Param("content") String content, Pageable pageable);

}




