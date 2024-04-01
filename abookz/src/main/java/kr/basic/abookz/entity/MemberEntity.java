package kr.basic.abookz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter

public class MemberEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEM_ID", nullable = false)
  private Long id;//pk
  private String loginId;//로그인시 필요한 아이디
  private String password;//비밀번호
  private String email;//이메일
  private String nickname;//닉네임(댓글,리뷰)
  @Enumerated(EnumType.STRING)
  private RoleEntity role;//역할 ADMIN,MANAGER,USER(Default = USER)
  private String profile; //프로필 이미지
  private LocalDateTime regDate;//가입일
//  @OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//  private List<ReviewEntity> reviewList = new ArrayList<>(); //회원이 작성한 리뷰 리스트
//@OneToMany(mappedBy = "member",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
//  private List<MemoEntity> memoList = new ArrayList<>(); //회원이 작성한 메모 리스트
}
