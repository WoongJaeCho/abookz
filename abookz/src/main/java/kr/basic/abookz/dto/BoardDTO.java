package kr.basic.abookz.dto;

import kr.basic.abookz.entity.board.BoardEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
  private Long id; // 글번호
  private String title; // 제목
  private String writer; // 작성자
  private String contents; // 내용
  private int hits; // 조회수
  private LocalDate createDate; // 작성날짜
  private LocalDate updateDate; // 수정날짜

  public BoardDTO(Long id, String title, String writer, int hits, LocalDate createDate) {
    this.id = id;
    this.title = title;
    this.writer = writer;
    this.hits = hits;
    this.createDate = createDate;
  }

  public static BoardDTO toBoardDTO(BoardEntity boardEntity){
    BoardDTO boardDTO = new BoardDTO();
    boardDTO.setId(boardEntity.getId());
    boardDTO.setTitle(boardEntity.getTitle());
    boardDTO.setWriter(boardEntity.getWriter());
    boardDTO.setContents(boardEntity.getContents());
    boardDTO.setHits(boardEntity.getHits());
    boardDTO.setCreateDate(boardEntity.getCreateDate());
    boardDTO.setUpdateDate(boardEntity.getUpdateDate());
    return boardDTO;
  }
}
