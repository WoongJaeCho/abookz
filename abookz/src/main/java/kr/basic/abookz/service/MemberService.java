package kr.basic.abookz.service;

import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
  // 생성자 주입
  private final MemberRepository memberRepository;

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
  public MemberDTO updateForm(String getId) {
    Optional<MemberEntity> byLoginId = memberRepository.findByLoginId(getId);
    if(byLoginId.isPresent()){
      return MemberDTO.AllMemberDTO(byLoginId.get());
    }
    else {
      return null;
    }
  }

  public void update(MemberDTO memberDTO) {
    memberRepository.save(MemberEntity.toupdateMemberEntity(memberDTO));
    // 파일 첨부 여부에 따라 로직 분리
//    if(memberDTO.getFile().isEmpty()){
//      // 첨부 파일 없음
//    }
//    else{
//      // 첨부 파일 있음
//      // 1. DTO에 담긴 파일을 꺼냄
//      // 2. 파일의 이름 가져옴
//      // 3. 서버 저장용 이름을 만듬
//      // 4. 저장 경로 설정
//      // 5. 해당 경로에 파일 저장
//      // 6. member에 해당 데이터 save 처리
//      // 7. fileentity 데이터 save 처리(보류)
//      MultipartFile profile = memberDTO.getFile(); // 1.
//      String originalFilename = profile.getOriginalFilename(); // 2.
//      String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
//      String savePath = "C:/abookz/abookz/src/main/resources/static/images/" + storedFileName;
//      profile.transferTo(new File(savePath)); // 5.
//    }
  }

  // 회원삭제
  public void deleteById(Long id) {
    memberRepository.deleteById(id);
  }
}
