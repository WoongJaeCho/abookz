package kr.basic.abookz.entity.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "BOOK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOOK_ID", nullable = false)
  private Long id;//pk

  private String title;//제목
  private String author;//저자
  private String publisher;//출판사
  private LocalDate pubDate;//출판일
  private Long ISBN;//isbn 10자리
  private Long ISBN13;//isbn 13자리
  @Enumerated(EnumType.STRING)
  private CategoryEnum categoryName;//카테고리
  private String cover;//커버이미지
  private String description;//요약
  private int itemPage;//상품쪽수
  private String link;//알라딘 상품 정보 페이지
//  private int weight;//무게
//  private int price;//가격
}
