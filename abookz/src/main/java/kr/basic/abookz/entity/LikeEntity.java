package kr.basic.abookz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "LIKE")
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
  @JoinColumn(name = "MEM_ID")
  private MemberEntity member;//좋아요 누른 회원

  @ManyToOne
  @JoinColumn(name = "REVIEW_ID")
  private ReviewEntity review;//좋아요가 있는 게시물
}
