package kr.basic.abookz.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDTO {
    private Long id;

    private  String Comment;

    private LocalDateTime createdDate;

    private MemberDTO memberDTO;

    private  BoardDTO boardDTO;
}
