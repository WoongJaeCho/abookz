package kr.basic.abookz.dto;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
  private LocalDateTime createdDate;//작성날짜
  private Boolean isSpoilerActive;//스포일러 방지기능
  private boolean liked; // Indicates if the logged-in user liked this review
  private BookShelfDTO bookShelfDTO;

}
