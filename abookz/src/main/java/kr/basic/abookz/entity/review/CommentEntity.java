package kr.basic.abookz.entity.review;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "COMMENT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COMMENT_ID", nullable = false)
  private Long id;//pk

  private String comment;//댓글내용

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEM_ID",foreignKey = @ForeignKey(name = "COMMENT_IBFK_1"))
  private MemberEntity member; // 리뷰 작성 회원

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "REVIEW_ID",foreignKey = @ForeignKey(name="COMMENT_IBFK_2"))
  private ReviewEntity review;//좋아요가 있는 게시물

  private LocalDateTime createdDate;//댓글 작성 날짜

}
