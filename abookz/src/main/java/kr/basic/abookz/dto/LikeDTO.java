package kr.basic.abookz.dto;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LikeDTO {

  private Long id;//pk


  private MemberDTO member; // 리뷰 작성 회원


  private ReviewDTO review;//좋아요가 있는 게시물
}
