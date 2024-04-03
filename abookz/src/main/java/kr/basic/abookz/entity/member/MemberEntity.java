package kr.basic.abookz.entity.member;

import jakarta.persistence.*;
import kr.basic.abookz.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

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
  private RoleEnum role;//역할 ADMIN,MANAGER,USER(Default = USER)
  private String profile; //프로필 이미지
  @CreationTimestamp
  private LocalDate regDate;//가입일
  // private int fileAttached; // 파일있으면 1, 없으면 0
//  private int challenge;//습관형성 챌린지
//  @OneToMany(mappedBy = "member",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//  private List<ReviewEntity> reviewList = new ArrayList<>(); //회원이 작성한 리뷰 리스트
//@OneToMany(mappedBy = "member",fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
//  private List<MemoEntity> memoList = new ArrayList<>(); //회원이 작성한 메모 리스트

  public static MemberEntity toMemberEntity(MemberDTO memberDTO){
    MemberEntity memberEntity = new MemberEntity();
    memberEntity.setLoginId(memberDTO.getLoginId());
    memberEntity.setPassword(memberDTO.getPassword());
    memberEntity.setEmail(memberDTO.getEmail());
    memberEntity.setNickname(memberDTO.getNickname());
    memberEntity.setRole(RoleEnum.Role_User);
    memberEntity.setProfile(memberDTO.getProfile());
    memberEntity.setRegDate(memberDTO.getRegDate());
    //memberEntity.setFileAttached(0); // 파일 없음
    return memberEntity;
  }

  public static MemberEntity toupdateMemberEntity(MemberDTO memberDTO){
    MemberEntity memberEntity = new MemberEntity();
    memberEntity.setId(memberDTO.getId());
    memberEntity.setLoginId(memberDTO.getLoginId());
    memberEntity.setPassword(memberDTO.getPassword());
    memberEntity.setEmail(memberDTO.getEmail());
    memberEntity.setNickname(memberDTO.getNickname());
    return memberEntity;
  }

}
