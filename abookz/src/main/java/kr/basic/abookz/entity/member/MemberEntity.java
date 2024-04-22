package kr.basic.abookz.entity.member;

import jakarta.persistence.*;
import kr.basic.abookz.dto.MemberDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

import static kr.basic.abookz.entity.member.RoleEnum.ROLE_USER;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MemberEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "MEM_ID", nullable = false)
  private Long id;//pk
  private String loginId;//로그인시 필요한 아이디(이메일)
  private String password;//비밀번호
  private String email;//이메일
  private String name;//닉네임(댓글,리뷰)
  @Enumerated(EnumType.STRING)
  private RoleEnum role;//역할 ADMIN,MANAGER,USER(Default = USER)
  private String profile; //프로필 이미지
  @CreationTimestamp
  private LocalDate regDate;//가입일

  public MemberEntity(String loginId) {
    this.loginId = loginId;

  }

  //OAuth 를 위해 추가하는 필드
  private String provider;
  private String providerId;

  //  private int challenge;//습관형성 챌린지
//  @OneToMany(mappedBy = "member, , orphanRemoval = true)
//  private List<ReviewEntity> reviewList = new ArrayList<>(); //회원이 작성한 리뷰 리스트
//@OneToMany(mappedBy = "member" ,, orphanRemoval = true)
//  private List<MemoEntity> memoList = new ArrayList<>(); //회원이 작성한 메모 리스트

  public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
    MemberEntity memberEntity = new MemberEntity();
    memberEntity.setLoginId(memberDTO.getLoginId());
    memberEntity.setPassword(memberDTO.getPassword());
    memberEntity.setEmail(memberDTO.getEmail());
    memberEntity.setName(memberDTO.getName());
    memberEntity.setRole(ROLE_USER);
    memberEntity.setRegDate(memberDTO.getRegDate());
    return memberEntity;
  }

  public static MemberEntity toupdateMemberEntity(MemberDTO memberDTO) {
    MemberEntity memberEntity = new MemberEntity();
    memberEntity.setId(memberDTO.getId());
    memberEntity.setLoginId(memberDTO.getLoginId());
    memberEntity.setPassword(memberDTO.getPassword());
    memberEntity.setEmail(memberDTO.getEmail());
    memberEntity.setName(memberDTO.getName());
    memberEntity.setProfile(memberDTO.getProfile());
    memberEntity.setRole(memberDTO.getRole());
    memberEntity.setRegDate(memberDTO.getRegDate());
    return memberEntity;
  }

  @Builder
  public MemberEntity(String loginId, String password, String email,String profile ,String provider, String providerId, String name) {
    this.loginId = loginId;
    this.password = password;
    this.email = email;
    this.profile = profile;
    this.provider = provider;
    this.providerId = providerId;
    this.role = ROLE_USER;
    this.name = name;
  }

  @PrePersist
  protected void onPersist() {
    if (this.profile == null || this.profile.trim().isEmpty()) {
      this.profile = "/images/default_profile.png"; // 기본 프로필 이미지 경로 설정
    }
  }
  @PreUpdate
  protected void onUpdate(){
    if(this.profile == null || this.profile.trim().isEmpty()){
      this.profile = "/images/default_profile.png"; // 기본 프로필 이미지 경로 설정
    }
  }
}
