package kr.basic.abookz.service;

import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.MemberEntity;
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
  // 생성자 주입
  private final MemberRepository memberRepository;

  public void save(MemberDTO memberDTO){
    // 1. dto -> entity 변환
    // 2. repository의 save메서드 호출

    // repository의 save메서드 호출
    MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
    memberRepository.save(memberEntity);
  }

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
        MemberDTO dto = MemberDTO.loginMemberDTO(memberEntity);
        return dto;
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
  }

  public void deleteById(Long id) {
    memberRepository.deleteById(id);
  }
}
