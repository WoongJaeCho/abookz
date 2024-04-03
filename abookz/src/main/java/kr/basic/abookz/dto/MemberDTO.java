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
  private String nickname; // 닉네임
  @Enumerated(EnumType.STRING)
  private RoleEnum role; // 역할
  private String profile; // 프로필이미지
  @CreationTimestamp
  private LocalDate regDate; // 가입일

<<<<<<< HEAD
=======
//  private MultipartFile file; // update.html -> Controller 파일 담는 용도
//  private String storedFileName; // 서버 저장용 파일 이름
//  private String originalFileName; // 원본 파일 이름(사용자가 지정한 이름)
//  private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)
>>>>>>> 61ba892300f44b1239d7cc83f434f0e7679d0b73

  public static MemberDTO loginMemberDTO(MemberEntity memberEntity){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(memberEntity.getId());
    memberDTO.setLoginId(memberEntity.getLoginId());
    memberDTO.setPassword(memberEntity.getPassword());
<<<<<<< HEAD
=======
    return memberDTO;
  }

  public static MemberDTO loginMember(String id, String pw){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setLoginId(id);
    memberDTO.setPassword(pw);
>>>>>>> 61ba892300f44b1239d7cc83f434f0e7679d0b73
    return memberDTO;
  }

  public static MemberDTO loginMember(String id, String pw){
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setLoginId(id);
    memberDTO.setPassword(pw);
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
