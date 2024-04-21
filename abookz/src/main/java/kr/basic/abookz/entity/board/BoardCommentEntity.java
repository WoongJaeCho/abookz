package kr.basic.abookz.entity.board;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDateTime;
@Table(name = "BOARD_COMMENT")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BoardCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="BOARD_COMMENT_ID",nullable = false)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID",foreignKey = @ForeignKey(name = "BOARD_COMMENT_IBFK_1"))
    private MemberEntity member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID", foreignKey = @ForeignKey(name = "BOARD_COMMENT_IBFK_2"))
    private BoardEntity board;

}
