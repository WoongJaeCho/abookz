package kr.basic.abookz.entity.book;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOOKSHELF")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"member" ,"book"})
public class BookShelfEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOOKSHELF_ID", nullable = false)
  private Long id;//pk
  @CreationTimestamp
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime addDate;//등록날짜
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime startDate;//읽기시작한날짜

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime endDate;//다읽은날짜

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDateTime targetDate;//목표날짜

  private int dailyPage;//하루목표 페이지
  //50page
  private int currentPage; //현재읽은페이지
  private Double bookShelfGrade;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "VARCHAR(20) DEFAULT 'WANT_TO_READ'")
  private TagEnum tag; // READ,WANT_TO_READ,CURRENTLY_READING

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "MEM_ID",foreignKey = @ForeignKey(name = "BOOKSHELF_IBFK_1"))
  private MemberEntity member; // 리뷰 작성 회원
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOK_ID", foreignKey = @ForeignKey(name = "BOOKSHELF_IBFK_2"))
  private BookEntity book;


@OneToMany(mappedBy = "bookShelf", orphanRemoval = true)
private List<MemoEntity> childListMemo = new ArrayList<>();

@OneToMany(mappedBy = "bookShelf", orphanRemoval = true)
private List<ReviewEntity> childListReview = new ArrayList<>();

  @Override
  public String toString() {
    return "BookShelfEntity{" +
            "id=" + id +
            ", addDate=" + addDate +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", targetDate=" + targetDate +
            ", dailyPage=" + dailyPage +
            ", currentPage=" + currentPage +
            ", bookShelfGrade=" + bookShelfGrade +
            ", tag=" + tag +
            ", member=" + member +
            ", book=" + book +
            '}';
  }

}
