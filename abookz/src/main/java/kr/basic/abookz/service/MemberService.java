package kr.basic.abookz.service;

import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
  // 생성자 주입
  private final MemberRepository memberRepository;
  private final JavaMailSender javaMailSender;


  @Value("${upload.path}" + "profile/")
  private String uploadPath;

//  public void sendSimpleMessage(MailDTO mailDTO){
//    SimpleMailMessage message = new SimpleMailMessage();
//    message.setFrom("ghrt8426@gmail.com");
//    message.setTo(mailDTO.getAddress());
//    message.setSubject(mailDTO.getTitle());
//    message.setText(mailDTO.getContent());
//    javaMailSender.send(message);
//  }


  // 회원가입
  public void save(MemberDTO memberDTO){
    // 1. dto -> entity 변환
    // 2. repository의 save메서드 호출

    // repository의 save메서드 호출
    MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
    memberRepository.save(memberEntity);
  }
  public boolean validById(String id) {
    return memberRepository.existsByLoginId(id);
  }

  // 로그인
  public MemberDTO login(MemberDTO memberDTO) {
    // 1. 회원이 입력한 아이디로 db조회
    // 2. db에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 맞는지 판단
    Optional<MemberEntity> byLoginId = memberRepository.findByLoginId(memberDTO.getLoginId());
    if(byLoginId.isPresent()){
      // 조회 결과가 있다(해당 아이디를 가진 회원이 있다)
      MemberEntity memberEntity = byLoginId.get();
      if(memberEntity.getPassword().equals(memberDTO.getPassword())){
        // 비밀번호 일치
        // entity -> dto 변환 후 리턴
        return MemberDTO.loginMemberDTO(memberEntity);
      }
      else {
        // 비밀번호 불일치
        return null;
      }
    }
    else {
      // 조회 결과가 없다(해당 아이디를 가진 회원이 없다)
      return null;
    }
  }

  // 회원조회
  public List<MemberDTO> findAll() {
    List<MemberEntity> Entitylist = memberRepository.findAll();
    List<MemberDTO> DTOList = new ArrayList<>();
    for (MemberEntity e : Entitylist) {
      DTOList.add(MemberDTO.AllMemberDTO(e));
    }
    return DTOList;

  }
  public MemberDTO findById(Long id) {
    Optional<MemberEntity> entity = memberRepository.findById(id);
    if(entity.isPresent()){
      return MemberDTO.AllMemberDTO(entity.get());
    }
    else {
      return null;
    }
  }

  // 회원수정
  public MemberDTO updateForm(Long getId) {
    Optional<MemberEntity> byId = memberRepository.findById(getId);
    if(byId.isPresent()){
      return MemberDTO.AllMemberDTO(byId.get());
    }
    else {
      return null;
    }
  }

  public void update(MemberDTO memberDTO, MultipartFile file) {
    try{
      if(file != null && !file.isEmpty()){
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        memberDTO.setProfile(fileName);

        Path upload = Paths.get(uploadPath);
        if(!Files.exists(upload)){
          Files.createDirectories(upload);
        }
        Path filePath = upload.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      }
      else {
        memberDTO.setProfile(memberDTO.getProfile());
      }
    }catch (Exception e){
      e.printStackTrace();
    }

    memberRepository.save(MemberEntity.toupdateMemberEntity(memberDTO));
  }

  // 회원삭제
  public void deleteById(Long id) {
    memberRepository.deleteById(id);
  }

  // 아이디 찾기
  public String findByEmail(MemberDTO memberDTO) {
    Optional<MemberEntity> byEmail = memberRepository.findByEmail(memberDTO.getEmail());
    if(byEmail.isPresent()){
      MemberEntity memberEntity = byEmail.get();
      return MemberDTO.findloginIdMember(memberEntity);
    }
    else {
      return null;
    }
  }
}
