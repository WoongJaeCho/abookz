package kr.basic.abookz.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDTO {
  private Long id;//pk
  private String comment;//댓글내용
  private MemberDTO member; // 리뷰 작성 회원
  private ReviewDTO review;//좋아요가 있는 게시물
  private LocalDateTime createdDate;//댓글 작성 날짜
}
