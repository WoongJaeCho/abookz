package kr.basic.abookz.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemoDTO {
    private Long id;
    private int page;//인용구나 느낀점 페이지
    private String quotes;//인상깊었던 인용구
    private String note;//느낀점
    private LocalDateTime createdDate;//작성날짜

    @Override
    public String toString() {
        return "MemoDTO{" +
                "id=" + id +
                ", page=" + page +
                ", quotes='" + quotes + '\'' +
                ", note='" + note + '\'' +
                ", createdDate=" + createdDate +
                ", bookShelf=" + bookShelf +
                '}';
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "BOOKSHELF_ID",foreignKey = @ForeignKey(name = "MEMODTO_IBFK_1"))
    private BookShelfEntity bookShelf;

    public static MemoDTO toMemoDTO(MemoEntity memoEntity){
        MemoDTO memoDTO = new MemoDTO();
        memoDTO.setId(memoEntity.getId());
        memoDTO.setPage(memoEntity.getPage());
        memoDTO.setQuotes(memoEntity.getQuotes());
        memoDTO.setNote(memoEntity.getNote());
        memoDTO.setCreatedDate(memoEntity.getCreatedDate());
        memoDTO.setBookShelf(memoEntity.getBookShelf());
        return memoDTO;
    }
}

