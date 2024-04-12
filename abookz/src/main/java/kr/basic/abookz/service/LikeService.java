package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import kr.basic.abookz.dto.*;
import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.LikeEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.repository.LikeRepository;
import kr.basic.abookz.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

  private final ReviewRepository reviewRepository;
  private final ModelMapper mapper;
  private final BookShelfService bookShelfService;
  private final EntityManager em;
  private final LikeRepository likeRepository;


  public List<LikeDTO> findAllByReview_Id(Long ReviewId) {
    List<LikeEntity> findLike = likeRepository.findAllByReview_Id(ReviewId);


    return findLike.stream()
        .map(this::mapEntityToDTO)
        .collect(Collectors.toList());
  }

  // DTO에서 Entity로 매핑
  public LikeEntity mapDTOToEntity(LikeDTO likeDTO) {
    LikeEntity likeEntity = mapper.map(likeDTO, LikeEntity.class);
    if (likeDTO.getMember() != null) {
      MemberEntity memberEntity = mapper.map(likeDTO.getMember(), MemberEntity.class);
      likeEntity.setMember(memberEntity);
    }
    if (likeDTO.getReview() != null) {
      ReviewEntity reviewEntity = mapper.map(likeDTO.getReview(), ReviewEntity.class);
      likeEntity.setReview(reviewEntity);
    }
    return likeEntity;
  }

  // Entity에서 DTO로 매핑
  public LikeDTO mapEntityToDTO(LikeEntity likeEntity) {
    LikeDTO likeDTO = mapper.map(likeEntity, LikeDTO.class);
    if (likeEntity.getMember() != null) {
      MemberDTO memberDTO = mapper.map(likeEntity.getMember(), MemberDTO.class);
      likeDTO.setMember(memberDTO);
    }
    if (likeEntity.getReview() != null) {
      ReviewDTO reviewDTO = mapper.map(likeEntity.getReview(), ReviewDTO.class);
      likeDTO.setReview(reviewDTO);
    }
    return likeDTO;
  }

}
