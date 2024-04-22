package kr.basic.abookz.entity.board;

import jakarta.persistence.*;
import kr.basic.abookz.dto.BoardDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.MemoEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BOARD")
@ToString
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
  @Enumerated(EnumType.STRING)
  private Category category; // 카테고리
  @CreationTimestamp
  private LocalDate createDate; // 작성날짜
  @UpdateTimestamp
  private LocalDate updateDate; // 수정날짜

  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "MEM_ID", foreignKey = @ForeignKey(name = "BOARD_IBFK_1"))
  private MemberEntity member; // 리뷰 작성 회원

  @OneToMany(mappedBy = "board", orphanRemoval = true)
  private List<BoardCommentEntity> childListBoard = new ArrayList<>();

  public static BoardEntity toSaveEntity(BoardDTO boardDTO){
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setTitle(boardDTO.getTitle());
    boardEntity.setWriter(boardDTO.getWriter());
    boardEntity.setContents(boardDTO.getContents());
    boardEntity.setHits(0);
    boardEntity.setCategory(boardDTO.getCategory());
    return boardEntity;
  }

  public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
    BoardEntity boardEntity = new BoardEntity();
    boardEntity.setId(boardDTO.getId());
    boardEntity.setTitle(boardDTO.getTitle());
    boardEntity.setWriter(boardDTO.getWriter());
    boardEntity.setContents(boardDTO.getContents());
    boardEntity.setHits(boardDTO.getHits());
    boardEntity.setCategory(boardDTO.getCategory());
    boardEntity.setCreateDate(boardDTO.getCreateDate());
    boardEntity.setUpdateDate(boardDTO.getUpdateDate());
    return boardEntity;
  }
}
