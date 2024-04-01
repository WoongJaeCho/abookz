package kr.basic.abookz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.nio.channels.Pipe;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

  @ManyToOne
  @JoinColumn(name = "MEM_ID")
  private MemberEntity member;//댓글 작성자

  @ManyToOne
  @JoinColumn(name = "REVIEW_ID")
  private ReviewEntity review;//댓글 작성한 리뷰

  private LocalDateTime createdDate;//댓글 작성 날짜

}
