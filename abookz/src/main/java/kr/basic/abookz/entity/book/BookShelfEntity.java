package kr.basic.abookz.entity.book;

import jakarta.persistence.*;
import kr.basic.abookz.entity.member.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOOKSHELF")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookShelfEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOOKSHELF_ID", nullable = false)
  private Long id;

  private LocalDateTime addDate;
  private LocalDateTime startDate;
  private LocalDateTime endDate;

  @Enumerated(EnumType.STRING)
  private TagEnum tag; // READ,WANT_TO_READ,CURRENTLY_READING

  @ManyToOne
  @JoinColumn(name = "MEM_ID",foreignKey = @ForeignKey(name = "BOOKSHELF_IBFK_1"))
  private MemberEntity member; // 리뷰 작성 회원

  @ManyToOne
  @JoinColumn(name = "BOOK_ID",foreignKey = @ForeignKey(name = "BOOKSHELF_IBFK_2"))
  private BookEntity book;
}
