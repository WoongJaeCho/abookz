package kr.basic.abookz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.awt.print.Book;
import java.time.LocalDateTime;

@Entity
@Table(name = "REVIEW")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVIEW_ID", nullable = false)
  private Long id;//pk

  private String title;//제목
  private String content;//내용
  private int grade;//평점(1,2,3,4,5) 별표시
  private LocalDateTime createdDate;//작성날짜
  private int count;//조회수
  private Boolean isSpoilerActive;//스포일러 방지기능
  //  @OneToMany(mappedBy = "like")
  //  private List<Like> likeList= new ArrayList<>(); 리뷰의 좋아요
  @ManyToOne
  @JoinColumn(name = "MEM_ID")
  private MemberEntity member; // 리뷰 작성 회원

}
