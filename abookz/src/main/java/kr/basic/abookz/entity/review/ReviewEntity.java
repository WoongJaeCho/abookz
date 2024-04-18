package kr.basic.abookz.entity.review;

import jakarta.persistence.*;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.book.BookEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "REVIEW")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"bookShelf"})
public class ReviewEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "REVIEW_ID", nullable = false)
  private Long id;//pk

  private String content;//내용
  @CreationTimestamp
  private LocalDateTime createdDate;//작성날짜
  private Boolean isSpoilerActive;//스포일러 방지기능
  //  @OneToMany(mappedBy = "like",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
  //  private List<Like> likeList= new ArrayList<>(); 리뷰의 좋아요
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOKSHELF_ID",foreignKey = @ForeignKey(name = "REVIEW_IBFK_1"))
  private BookShelfEntity bookShelf;
}
