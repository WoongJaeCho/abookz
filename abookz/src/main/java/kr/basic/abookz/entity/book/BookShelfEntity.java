package kr.basic.abookz.entity.book;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKSHELF")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookShelfEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOOKSHELF_ID", nullable = false)
  private Long id;//pk

  @CreationTimestamp
  private LocalDateTime addDate;//등록날짜
  private LocalDateTime startDate;//읽기시작한날짜
  private LocalDateTime endDate;//다읽은날짜
  private LocalDateTime targetDate;//목표날짜
  private int dailyPage;//하루목표 페이지
  private int currentPage; //현재읽은페이지
  private int bookShelfGrade;//평점(1,2,3,4,5) 별표시


  @Enumerated(EnumType.STRING)
  private TagEnum tag; // READ,WANT_TO_READ,CURRENTLY_READING

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEM_ID",foreignKey = @ForeignKey(name = "BOOKSHELF_IBFK_1"))
  private MemberEntity member; // 리뷰 작성 회원

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOK_ID",foreignKey = @ForeignKey(name = "BOOKSHELF_IBFK_2"))
  private BookEntity book;

  @PrePersist
  public void prePersist() {
    this.addDate = LocalDateTime.now().withSecond(0).withNano(0);
  }

}
