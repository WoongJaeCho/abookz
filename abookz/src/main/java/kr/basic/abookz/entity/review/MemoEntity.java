package kr.basic.abookz.entity.review;

import jakarta.persistence.*;
import kr.basic.abookz.dto.MemoDTO;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.book.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemoEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEMO_ID", nullable = false)
  private Long id;
  private int page;//인용구나 느낀점 페이지
  private String quotes;//인상깊었던 인용구
  private String note;//느낀점
  private LocalDateTime createdDate;//작성날짜
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOOKSHELF_ID",foreignKey = @ForeignKey(name = "MEMO_IBFK_1"))
  private BookShelfEntity bookShelf;

  @Override
  public String toString() {
    return "MemoEntity{" +
            "id=" + id +
            ", page=" + page +
            ", quotes='" + quotes + '\'' +
            ", note='" + note + '\'' +
            ", createdDate=" + createdDate +
            ", bookShelf=" + bookShelf +
            '}';
  }

  public static MemoEntity toMemoEntity(MemoDTO memoDTO){
    MemoEntity memoEntity = new MemoEntity();
    memoEntity.setId(memoDTO.getId());
    memoEntity.setPage(memoDTO.getPage());
    memoEntity.setQuotes(memoDTO.getQuotes());
    memoEntity.setNote(memoDTO.getNote());
    memoEntity.setCreatedDate(memoDTO.getCreatedDate());
    memoEntity.setBookShelf(memoDTO.getBookShelf());
    return memoEntity;
  }
}
