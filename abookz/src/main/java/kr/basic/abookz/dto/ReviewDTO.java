package kr.basic.abookz.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdDate;//작성날짜
  private Boolean isSpoilerActive;//스포일러 방지기능
  private boolean liked;
  private int likesCount;
  private BookShelfDTO bookShelfDTO;

}
