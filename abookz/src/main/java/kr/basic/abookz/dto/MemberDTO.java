package kr.basic.abookz.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.member.RoleEnum;
import lombok.*;

import java.time.LocalDateTime;

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
  private String nickname; // 닉네임
  @Enumerated(EnumType.STRING)
  private RoleEnum role; // 역할
  private String profile; // 프로필이미지
  private LocalDateTime regDate; // 가입일

  public static MemberDTO loginMemberDTO(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberEntity.getId());
    memberDTO.setLoginId(memberEntity.getLoginId());
    memberDTO.setPassword(memberEntity.getPassword());
    return memberDTO;
  }

  public static MemberDTO updateMemberDTO(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberEntity.getId());
    memberDTO.setLoginId(memberEntity.getLoginId());
    memberDTO.setPassword(memberEntity.getPassword());
    memberDTO.setEmail(memberEntity.getEmail());
    memberDTO.setNickname(memberEntity.getNickname());
    return memberDTO;
  }


  public static MemberDTO AllMemberDTO(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberEntity.getId());
    memberDTO.setLoginId(memberEntity.getLoginId());
    memberDTO.setPassword(memberEntity.getPassword());
    memberDTO.setEmail(memberEntity.getEmail());
    memberDTO.setNickname(memberEntity.getNickname());
    memberDTO.setRole(memberEntity.getRole());
    memberDTO.setProfile(memberEntity.getProfile());
    memberDTO.setRegDate(memberEntity.getRegDate());
    return memberDTO;
  }
}
