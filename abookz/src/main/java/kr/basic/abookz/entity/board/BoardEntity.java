package kr.basic.abookz.entity.board;

import jakarta.persistence.*;
import kr.basic.abookz.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "BOARD")
public class BoardEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOARD_ID", nullable = false)
  private Long id; // 글번호
  private String title; // 제목
  private String writer; // 작성자
  @Column(length = 1000)
  private String contents; // 내용
  private int hits; // 조회수
  @CreationTimestamp
  private LocalDate createDate; // 작성날짜
  @UpdateTimestamp
  private LocalDate updateDate; // 수정날짜

  public static BoardEntity toSaveEntity(BoardDTO boardDTO){
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setTitle(boardDTO.getTitle());
    boardEntity.setWriter(boardDTO.getWriter());
    boardEntity.setContents(boardDTO.getContents());
    boardEntity.setHits(0);
    return boardEntity;
  }

  public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setId(boardDTO.getId());
    boardEntity.setTitle(boardDTO.getTitle());
    boardEntity.setWriter(boardDTO.getWriter());
    boardEntity.setContents(boardDTO.getContents());
    boardEntity.setHits(boardDTO.getHits());
    boardEntity.setCreateDate(boardDTO.getCreateDate());
    boardEntity.setUpdateDate(boardDTO.getUpdateDate());
    return boardEntity;
  }
}