package kr.basic.abookz.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatingDTO {
  private Long bookShelfId;
  private Long bookId;
  private Double rating;
}
