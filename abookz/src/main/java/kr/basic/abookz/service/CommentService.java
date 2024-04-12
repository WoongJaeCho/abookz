package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import kr.basic.abookz.dto.CommentDTO;
import kr.basic.abookz.dto.LikeDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.dto.ReviewDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.LikeEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.repository.CommentRepository;
import kr.basic.abookz.repository.LikeRepository;
import kr.basic.abookz.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

  private final ReviewRepository reviewRepository;
  private final ModelMapper mapper;
  private final BookShelfService bookShelfService;
  private final EntityManager em;
  private final CommentRepository commentRepository;

  public List<CommentDTO> findAllByReview_IdAndMember_Id(Long reviewId, Long memberId){
    List<CommentEntity> findLike = commentRepository.findAllByReview_IdAndMember_Id(reviewId,memberId);

    return findLike.stream()
        .map(this::mapEntityToDTO)
        .collect(Collectors.toList());
  }

  // DTO에서 Entity로 매핑
  public CommentEntity mapDTOToEntity(CommentDTO commentDTO) {
    CommentEntity commentEntity = mapper.map(commentDTO, CommentEntity.class);
    if (commentDTO.getMember() != null) {
      MemberEntity memberEntity = mapper.map(commentDTO.getMember(), MemberEntity.class);
      commentEntity.setMember(memberEntity);
    }
    if (commentDTO.getReview() != null) {
      ReviewEntity reviewEntity = mapper.map(commentDTO.getReview(), ReviewEntity.class);
      commentEntity.setReview(reviewEntity);
    }
    return commentEntity;
  }

  // Entity에서 DTO로 매핑
  public CommentDTO mapEntityToDTO(CommentEntity commentEntity) {
    CommentDTO commentDTO = mapper.map(commentEntity, CommentDTO.class);
    if (commentEntity.getMember() != null) {
      MemberDTO memberDTO = mapper.map(commentEntity.getMember(), MemberDTO.class);
      commentDTO.setMember(memberDTO);
    }
    if (commentEntity.getReview() != null) {
      ReviewDTO reviewDTO = mapper.map(commentEntity.getReview(), ReviewDTO.class);
      commentDTO.setReview(reviewDTO);
    }
    return commentDTO;
  }

}
