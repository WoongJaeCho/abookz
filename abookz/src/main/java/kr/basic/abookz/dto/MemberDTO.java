package kr.basic.abookz.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.member.RoleEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
  private Long id; // pk
  private String loginId; // 아이디
  private String password; // 비번
  private String email; // 이메일
  private String name; // 닉네임
  @Enumerated(EnumType.STRING)
  private RoleEnum role; // 역할
  private String profile; // 프로필이미지
  private LocalDate regDate; // 가입일

  public MemberDTO(Long id, String loginId, String password, String email, String name, RoleEnum role, LocalDate regDate, String profile) {
    this.id = id;
    this.loginId = loginId;
    this.password = password;
    this.email = email;
    this.name = name;
    this.role = role;
    this.regDate = regDate;
    this.profile = profile;
  }


  public static MemberDTO loginMemberDTO(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberEntity.getId());
    memberDTO.setLoginId(memberEntity.getLoginId());
    memberDTO.setPassword(memberEntity.getPassword());
    return memberDTO;
  }

  public static MemberDTO loginMember(String id, String pw){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setLoginId(id);
    memberDTO.setPassword(pw);
    return memberDTO;
  }

  public static String findloginIdMember(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setLoginId(memberEntity.getLoginId());
    return memberDTO.getLoginId();
  }

  public static MemberDTO AllMemberDTO(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberEntity.getId());
    memberDTO.setLoginId(memberEntity.getLoginId());
    memberDTO.setPassword(memberEntity.getPassword());
    memberDTO.setEmail(memberEntity.getEmail());
    memberDTO.setName(memberEntity.getName());
    memberDTO.setRole(memberEntity.getRole());
    memberDTO.setProfile(memberEntity.getProfile());
    memberDTO.setRegDate(memberEntity.getRegDate());
    return memberDTO;
  }


}
