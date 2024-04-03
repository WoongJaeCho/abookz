package kr.basic.abookz.entity.review;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REVIEW_LIKE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "LIKE_ID", nullable = false)
  private Long id;//pk

  @ManyToOne
  @JoinColumn(name = "MEM_ID",foreignKey = @ForeignKey(name = "LIKE_IBFK_1"))
  private MemberEntity member; // 리뷰 작성 회원

  @ManyToOne
  @JoinColumn(name = "REVIEW_ID",foreignKey = @ForeignKey(name="LIKE_IBFK_2"))
  private ReviewEntity review;//좋아요가 있는 게시물
}
