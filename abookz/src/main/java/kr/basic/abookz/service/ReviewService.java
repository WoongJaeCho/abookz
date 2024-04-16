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

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ModelMapper mapper;
    private final BookShelfService bookShelfService;
    private final EntityManager em;


    public ReviewEntity save(ReviewDTO reviewDTO)  {
        ReviewEntity reviewEntity = mapDTOToEntity(reviewDTO);
        return reviewRepository.save(reviewEntity);
    }

    public ReviewDTO findByBookShelfId(Long id){
        Optional<ReviewEntity> findReview = reviewRepository.findByBookShelfId(id);

        return mapEntityToDTO(findReview.orElse(null));
    }

    private ReviewDTO mapEntityToDTO(ReviewEntity entity) {
        if (entity ==null) {
            return null;
        }
        ReviewDTO reviewDTO = mapper.map(entity, ReviewDTO.class);
        BookShelfDTO bookShelfDTO = mapper.map(entity.getBookShelf(), BookShelfDTO.class);
        reviewDTO.setBookShelfDTO(bookShelfDTO);
        return  reviewDTO;
    }
    private ReviewEntity mapDTOToEntity(ReviewDTO reviewDTO) {
        ReviewEntity reviewEntity = mapper.map(reviewDTO, ReviewEntity.class);
        if(reviewDTO.getBookShelfDTO() != null){
            Long bookShelfId = reviewDTO.getBookShelfDTO().getId();
            BookShelfEntity bookShelfEntity = bookShelfService.mapDTOToEntity(bookShelfService.findById(bookShelfId)) ;
            reviewEntity.setBookShelf(bookShelfEntity);
        }
        return reviewEntity;
    }

    public Optional<ReviewEntity> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public ReviewEntity Update(ReviewDTO reviewDTO){
        ReviewEntity reviewEntity = reviewRepository.findById(reviewDTO.getId())
            .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewDTO.getId()));

        reviewEntity.setContent(reviewDTO.getContent());
        reviewEntity.setIsSpoilerActive(reviewDTO.getIsSpoilerActive());

        return reviewEntity;
    }

    public Page<ReviewEntity> getReviewsByBookId(Long bookId, Pageable pageable) {
        return reviewRepository.findByBookShelf_Book_Id(bookId,pageable);
    }

  public Page<ReviewEntity> findAll(Pageable pageable) {
      return reviewRepository.findAll(pageable);
  }
}
