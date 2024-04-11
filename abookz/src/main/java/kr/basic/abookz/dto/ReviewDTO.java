package kr.basic.abookz.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewDTO {

  private Long id;//pk

  private String content;//내용
  private Double reviewGrade;//평점(1,2,3,4,5) 별표시
  private LocalDateTime createdDate;//작성날짜
  private Boolean isSpoilerActive;//스포일러 방지기능
  private BookShelfDTO bookShelfDTO;
}
