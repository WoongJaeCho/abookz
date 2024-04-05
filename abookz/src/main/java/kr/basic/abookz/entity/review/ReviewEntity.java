package kr.basic.abookz.entity.review;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.book.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  private String content;//내용
  private int reviewGrade;//평점(1,2,3,4,5) 별표시
  private LocalDateTime createdDate;//작성날짜
  private Boolean isSpoilerActive;//스포일러 방지기능
  //  @OneToMany(mappedBy = "like",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
  //  private List<Like> likeList= new ArrayList<>(); 리뷰의 좋아요
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEM_ID",foreignKey = @ForeignKey(name = "REVIEW_IBFK_1"))
  private MemberEntity member; // 리뷰 작성 회원

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOK_ID",foreignKey = @ForeignKey(name = "REVIEW_IBFK_2"))
  private BookEntity book;
}
