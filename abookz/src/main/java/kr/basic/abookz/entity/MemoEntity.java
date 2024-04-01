package kr.basic.abookz.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMO")
public class MemoEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEMO_ID", nullable = false)
  private Long id;
  private int page;//인용구나 느낀점 페이지
  private String quotes;//인상깊었던 인용구
  private String note;//느낀점
  private LocalDateTime createdDate;//작성날짜
}
