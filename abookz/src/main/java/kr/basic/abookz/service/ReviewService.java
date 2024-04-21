package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import kr.basic.abookz.dto.*;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.repository.ReviewRepository;
import kr.basic.abookz.dto.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final ModelMapper mapper;
  private final BookShelfService bookShelfService;
  private final LikeService likeService;
  private final EntityManager em;


  public ReviewEntity save(ReviewDTO reviewDTO) {
    ReviewEntity reviewEntity = mapDTOToEntity(reviewDTO);
    return reviewRepository.save(reviewEntity);
  }

  public ReviewDTO findByBookShelfId(Long id) {
    Optional<ReviewEntity> findReview = reviewRepository.findByBookShelfId(id);

    return mapEntityToDTO(findReview.orElse(null));
  }

  private ReviewDTO mapEntityToDTO(ReviewEntity entity) {
    if (entity == null) {
      return null;
    }
    ReviewDTO reviewDTO = mapper.map(entity, ReviewDTO.class);
    BookShelfDTO bookShelfDTO = mapper.map(entity.getBookShelf(), BookShelfDTO.class);
    reviewDTO.setBookShelfDTO(bookShelfDTO);
    return reviewDTO;
  }

  private ReviewEntity mapDTOToEntity(ReviewDTO reviewDTO) {
    ReviewEntity reviewEntity = mapper.map(reviewDTO, ReviewEntity.class);
    if (reviewDTO.getBookShelfDTO() != null) {
      Long bookShelfId = reviewDTO.getBookShelfDTO().getId();
      BookShelfEntity bookShelfEntity = bookShelfService.mapDTOToEntity(bookShelfService.findById(bookShelfId));
      reviewEntity.setBookShelf(bookShelfEntity);
    }
    return reviewEntity;
  }

  public ReviewDTO findById(Long id) {
    return mapEntityToDTO(reviewRepository.findById(id).get());
  }

  public ReviewEntity Update(ReviewDTO reviewDTO) {
    ReviewEntity reviewEntity = reviewRepository.findById(reviewDTO.getId())
        .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewDTO.getId()));

    reviewEntity.setContent(reviewDTO.getContent());
    reviewEntity.setIsSpoilerActive(reviewDTO.getIsSpoilerActive());

    return reviewEntity;
  }

  public Page<ReviewEntity> getReviewsByISBN13(String ISBN13, Pageable pageable) {
    return reviewRepository.findByBookISBN13(ISBN13, pageable);
  }

  public Page<ReviewEntity> findAll(Pageable pageable) {
    return reviewRepository.findAll(pageable);
  }

  public Page<ReviewEntity> findReviewsByContent( String ISBN13, String query,Pageable pageable) {
    return reviewRepository.findByBookISBN13AndContent(ISBN13, query, pageable);
  }

  public Page<ReviewEntity> findReviewsByLikes(String ISBN13, String query, Pageable pageable) {
    return reviewRepository.findByBookISBN13AndContentSortedByLikes(ISBN13, query, pageable);
  }

  public Page<ReviewEntity> findMyReviewsByLikes(Long memberId,  Pageable pageable) {

    return reviewRepository.findByBookShelfMemberIdOrderedByLikesDesc(memberId,  pageable);
  }

  public Page<ReviewEntity> findMyReviews(Long memberId,  Pageable pageable) {
    return reviewRepository.findByBookShelf_Member_Id(memberId,  pageable);

  }

  public void deleteReviews(List<String> reviewIds) {

    List<Long> parsedIds = reviewIds.stream()
        .map(Long::parseLong)
        .collect(Collectors.toList());
    reviewRepository.deleteAllById(parsedIds);
  }
}
