package kr.basic.abookz.service;

import jakarta.persistence.EntityManager;
import kr.basic.abookz.dto.CommentDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.CommentEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.repository.CommentRepository;
import kr.basic.abookz.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

  public List<CommentDTO> findAllByReview_IdAndMember_Id(Long reviewId, Long memberId) {
    List<CommentEntity> findLike = commentRepository.findAllByReview_IdAndMember_Id(reviewId, memberId);

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
    if (commentDTO.getReviewId() != null) {
      ReviewEntity reviewEntity = reviewRepository.findById(commentDTO.getReviewId())
          .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));
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
      commentDTO.setReviewId(commentEntity.getReview().getId());
    }
    return commentDTO;
  }

  public Slice<CommentDTO> findAllByReview_Id(Long reviewId, Pageable pageable) {
    return commentRepository.findAllByReview_Id(reviewId, pageable)
        .map(this::mapEntityToDTO);
  }

  public CommentDTO save(CommentDTO comment) {
    return mapEntityToDTO(commentRepository.save(mapDTOToEntity(comment)));
  }

  public List<CommentDTO> findByReview_Id(Long id) {
    List<CommentEntity> findComment = commentRepository.findByReview_Id(id);
    return findComment.stream().map(this::mapEntityToDTO).collect(Collectors.toList());
  }

  public void deleteComment(Long id) {
    commentRepository.deleteById(id);
  }

  public MemberEntity getOwnerIdById(Long id) {
    return commentRepository.findById(id)
        .map(CommentEntity::getMember)
        .orElseThrow(() -> new NoSuchElementException("댓글을 찾을 수 없습니다."));
  }

  public CommentDTO findById(Long id) {
    return mapEntityToDTO(commentRepository.findById(id).get());
  }

  public void update(CommentDTO comment, String text) {
    Optional<CommentEntity> findComment = commentRepository.findById(comment.getId());
    findComment.get().setComment(text);
  }
}

