package kr.basic.abookz.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO{
  private String address;
  private String title;
  private String message;
}

