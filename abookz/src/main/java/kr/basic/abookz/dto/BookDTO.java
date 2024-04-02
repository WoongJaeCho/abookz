package kr.basic.abookz.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.basic.abookz.entity.BookEntity;
import kr.basic.abookz.entity.CategoryEntity;
import lombok.*;

import java.awt.print.Book;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;//pk
    private String title;//제목
    private String author;//저자
    private String publisher;//출판사
    private LocalDate pubDate;//출판일
    private String ISBN13;//isbn 13자리
    @Enumerated(EnumType.STRING)
    private CategoryEntity categoryName;//카테고리
    private String cover;//커버이미지
    private String description;//요약
    private int itemPage;//상품쪽수
    private String link;//알라딘 상품 정보 페이지
    private String ISBN;

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pubDate=" + pubDate +
                ", ISBN13=" + ISBN13 +
                ", categoryName=" + categoryName +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", itemPage=" + itemPage +
                ", link='" + link + '\'' +
                ", ISBN=" + ISBN +
                '}';
    }
}
