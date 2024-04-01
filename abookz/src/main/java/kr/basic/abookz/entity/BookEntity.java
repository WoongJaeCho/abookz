package kr.basic.abookz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "BOOK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOOK_ID", nullable = false)
  private Long id;//pk
  private String title;//제목
  private String author;//저자
  private String publisher;//출판사
  private LocalDate pubDate;//출판일
  private Long ISBN13;//isbn 13자리
  @Enumerated(EnumType.STRING)
  private CategoryEntity categoryName;//카테고리
  private String cover;//커버이미지
  private String description;//요약
  private int itemPage;//상품쪽수
  private String link;//알라딘 상품 정보 페이지
  private Long ISBN;
  public BookEntity(String title, String author, String publisher, LocalDate pubDate, Long ISBN13, /*CategoryEntity categoryName,*/ String cover, String description/*, int itemPage*/, String link) {
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.pubDate = pubDate;
    this.ISBN13 = ISBN13;
  /*  this.categoryName = categoryName;*/
    this.cover = cover;
    this.description = description;
  /*  this.itemPage = itemPage;*/
    this.link = link;
  }
  public BookEntity(String title, String author, String publisher, Long ISBN,LocalDate pubDate , /*CategoryEntity categoryName,*/ String cover, String description/*, int itemPage*/, String link) {
    this.title = title;
    this.author = author;
    this.publisher = publisher;
    this.pubDate = pubDate;
    this.ISBN = ISBN;
    /*  this.categoryName = categoryName;*/
    this.cover = cover;
    this.description = description;
    /*  this.itemPage = itemPage;*/
    this.link = link;
  }
}
