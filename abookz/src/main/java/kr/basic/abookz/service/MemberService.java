package kr.basic.abookz.service;



import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.EmailDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.BookRepository;
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static kr.basic.abookz.entity.book.TagEnum.READ;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
  // 생성자 주입
  private final MemberRepository memberRepository;
  private final JavaMailSender javaMailSender;
  private final BookShelfService bookShelfService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;


  @Value("${upload.path}" + "profile/")
  private String uploadPath;

  @Value("${spring.mail.username}")
  private String username;


  // 회원가입
  public void save(MemberDTO memberDTO){
    // 1. dto -> entity 변환
    // 2. repository의 save메서드 호출
    Optional<MemberEntity> byloginId = memberRepository.findByLoginId(memberDTO.getLoginId());
    if(byloginId.isEmpty()){
      // 비밀번호 암호화처리
      String initPassword = memberDTO.getPassword();
      String enPassword = bCryptPasswordEncoder.encode(initPassword);
      memberDTO.setPassword(enPassword);
      MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
      memberRepository.save(memberEntity);
    }
  }
  public boolean validById(String id) {
    return memberRepository.existsByLoginId(id);
  }

  // 로그인(시큐리티 사용으로 인한 주석처리)
//  public MemberDTO login(MemberDTO memberDTO) {
//    // 1. 회원이 입력한 아이디로 db조회
//    // 2. db에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 맞는지 판단
//    Optional<MemberEntity> byLoginId = memberRepository.findByLoginId(memberDTO.getLoginId());
//    if(byLoginId.isPresent()){
//      // 조회 결과가 있다(해당 아이디를 가진 회원이 있다)
//      MemberEntity memberEntity = byLoginId.get();
//      if(memberEntity.getPassword().equals(memberDTO.getPassword())){
//        // 비밀번호 일치
//        // entity -> dto 변환 후 리턴
//        return MemberDTO.loginMemberDTO(memberEntity);
//      }
//      else {
//        // 비밀번호 불일치
//        return null;
//      }
//    }
//    else {
//      // 조회 결과가 없다(해당 아이디를 가진 회원이 없다)
//      return null;
//    }
//  }


  // 회원조회
  public Page<MemberDTO> paging(Pageable pageable) {
    int page = pageable.getPageNumber() - 1;
    int pageLimit = 5; // 한 페이지에 보여줄 글 갯수
    Page<MemberEntity> memberEntities = memberRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
    return memberEntities.map(member -> new MemberDTO(member.getId(), member.getLoginId(), member.getPassword(), member.getEmail(), member.getName(), member.getRole(), member.getRegDate(), member.getProfile(), member.getProvider(), member.getProviderId()));
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
    Optional<MemberEntity> byId = memberRepository.findById(memberDTO.getId());
    if(byId.isPresent()){
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
          memberDTO.setProfile(byId.get().getProfile());
        }
        // 비밀번호 수정 시 다시 암호화하여 변경
        String encodePw = bCryptPasswordEncoder.encode(memberDTO.getPassword());
        memberDTO.setPassword(encodePw);
      }catch (Exception e){
        e.printStackTrace();
      }
    }
    memberRepository.save(MemberEntity.toupdateMemberEntity(memberDTO));
  }
  public void updateRole(MemberDTO memberDTO) {
    Optional<MemberEntity> byId = memberRepository.findById(memberDTO.getId());
    System.out.println("byId = " + byId);
    if(byId.isPresent()){
      MemberEntity entity = byId.get();
      entity.setRole(memberDTO.getRole());
      System.out.println("entity = " + entity);
    }
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
  // 임시 비밀번호 발급 및 변경
  public EmailDTO createMailAndChangePassword(String email, String loginId) {
    Optional<MemberEntity> memberLoginId = memberRepository.findByLoginIdAndEmail(loginId, email);
    if(memberLoginId.isPresent()){
      String str = getTempPassword();
      EmailDTO dto = new EmailDTO();
      dto.setAddress(email);
      dto.setTitle("abookz 임시 비밀번호 안내이메일 입니다.");
      dto.setMessage("안녕하세요. abookz 임시비밀번호 안내 관련 이메일입니다." +
          "회원님의 임시 비밀번호는 " + str + "입니다." + "로그인 후에 비밀번호를 변경 해 주세요.");
      updatePassword(str, loginId, email);
      return dto;
    }
    else {
      return null;
    }
  }
  // 임시 비밀번호 db 업데이트
  public void updatePassword(String str, String loginId, String email){
    try{
      Optional<MemberEntity> memberLoginId = memberRepository.findByLoginIdAndEmail(loginId, email);
      if(memberLoginId.isPresent()){
        MemberEntity memberEntity = memberLoginId.get();
        String encodePw = bCryptPasswordEncoder.encode(str);
        memberEntity.setPassword(encodePw);
        memberRepository.save(memberEntity);
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  // 임시 비밀번호 생성
  public String getTempPassword(){
    char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    String str = "";

    // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
    int idx = 0;
    for (int i = 0; i < 10; i++) {
      idx = (int) (charSet.length * Math.random());
      str += charSet[idx];
    }
    return str;
  }

  // 임시 비밀번호 이메일 발송

  public void mailSend(EmailDTO emailDTO){
    System.out.println("전송 완료!");
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(emailDTO.getAddress());
    message.setSubject(emailDTO.getTitle());
    message.setText(emailDTO.getMessage());
    message.setFrom(username);
    message.setReplyTo(username);
    System.out.println("message"+message);
    javaMailSender.send(message);
  }

  public Map<String, Double> calculateAverageWeightOfReadBooksByMember() {
    List<MemberEntity> members = memberRepository.findAll();
    Map<String, Double> memberAverages = new HashMap<>();

    for (MemberEntity member : members) {
      List<BookShelfDTO> shelves = bookShelfService.findAllByMemberIdAndTag(member.getId(), READ);
      double averageWeight = shelves.stream()
          .mapToDouble(shelf -> shelf.getBookDTO().getWeight())
          .average()
          .orElse(0.0); // 읽은 책이 없는 경우 0.0 반환
      memberAverages.put(member.getName(), averageWeight / 1000.0); // 책의 무게를 킬로그램으로 변환
    }

    return memberAverages;
  }

  public double calculateOverallAverageWeight() {
    Map<String, Double> memberAverages = calculateAverageWeightOfReadBooksByMember();
    double totalAverage = memberAverages.values().stream()
        .mapToDouble(Double::doubleValue)
        .average()
        .orElse(0.0); // 회원이 없는 경우 0.0 반환

    return totalAverage;
  }
}
