package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.EmailDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.member.RoleEnum;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static kr.basic.abookz.entity.member.RoleEnum.ROLE_ADMIN;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
  // 생성자 주입
  private final MemberService memberService;


  private boolean logincheck(@AuthenticationPrincipal PrincipalDetails principalDetails){
    if(principalDetails == null){
      return false;
    }
    return true;
  }

  // 조회
  @GetMapping("/list")
  public String findAll(@PageableDefault(page = 1) Pageable pageable, Model model,
                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
    // 현재 인증된 사용자의 세부 정보 가져오기

    MemberEntity member = principalDetails.getMember();
    if (member.getRole() == ROLE_ADMIN) {
      Page<MemberDTO> memberList = memberService.paging(pageable);
      int blockLimit = 5;
      int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
      int endPage = ((startPage + blockLimit - 1) < memberList.getTotalPages()) ? startPage + blockLimit - 1 : memberList.getTotalPages();

      model.addAttribute("memberList", memberList);
      model.addAttribute("startPage", startPage);
      model.addAttribute("endPage", endPage);
      return "member/list";
    }




    // 'ADMIN' 역할이 없으면 로그인 폼으로 리다이렉트
    return "redirect:/member/loginForm";
  }

  @GetMapping("/{id}")
  public String findById(@PathVariable Long id, Model model) {
    MemberDTO memberDTO = memberService.findById(id);
    model.addAttribute("member", memberDTO);
    return "member/detail";
  }

  // 가입
  @GetMapping("/save")
  public String saveForm() {
    return "member/save";
  }

  @PostMapping("/save")
  public String saveMember(@ModelAttribute MemberDTO memberDTO) {
    System.out.println("memberDTO = " + memberDTO);
    memberService.save(memberDTO);
    return "redirect:/";
  }

  @PostMapping("/validId")
  @ResponseBody
  public String validId(@RequestParam("id") String id) {
    System.out.println("id체크 = " + id);
    return memberService.validById(id) ? "notValid" : "valid";
  }

  // 로그인
  @GetMapping("/loginForm")
  public String loginForm() {
    return "member/loginForm";
  }

  // 시큐리티 사용으로 인해 주석 처리
  // @PostMapping("/login")
  // @ResponseBody
  // public String loginMember(@RequestParam("id") String id, @RequestParam("pw") String pw, HttpSession session){
  //   MemberDTO memberDTO = MemberDTO.loginMember(id, pw);
  //   MemberDTO loginResult = memberService.login(memberDTO);
  //   if(loginResult != null){
  //     // 로그인 성공시
  //     session.setAttribute("id", loginResult.getId());
  //     return "confirm";
  //   }
  //   else {
  //     // 로그인 실패시
  //     return "null";
  //   }
  // }


  // 수정
  @GetMapping("/update")
  public String updateForm(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (!logincheck(principalDetails)) {
      return "member/loginForm";
    }
    Long getId = principalDetails.getMember().getId();
    MemberDTO memberDTO = memberService.updateForm(getId);
    model.addAttribute("updateMember", memberDTO);
    return "member/update";
  }

  @PostMapping("/update")
  public String update(@ModelAttribute MemberDTO memberDTO, @RequestParam("file") MultipartFile file) {
    try {
      memberService.update(memberDTO, file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "redirect:/member/" + memberDTO.getId();
  }

  @PostMapping("/changeRole")
  @ResponseBody
  public String changeRole(@RequestBody MemberDTO memberDTO){
    System.out.println("memberDto = " + memberDTO);
    memberService.updateRole(memberDTO);
    return "confirm";
  }

  // 삭제
  @GetMapping("/delete/{id}")
  public String deleteById(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long id, HttpSession session) {
    if(!logincheck(principalDetails)){
      return "member/loginForm";
    }
    Long loggedInUserId = principalDetails.getMember().getId();
    if(id.equals(loggedInUserId)){
      session.invalidate();
    }
    memberService.deleteById(id);
    return "redirect:/member/list";
  }

  // 로그아웃
  @GetMapping("/logout")
  @ResponseBody
  public String logout(HttpSession session) {
    session.invalidate();
    return "confirm";
  }

  // 아이디 찾기
  @GetMapping("/loginIdfind")
  public String IdfindForm() {
    return "member/loginIdfind";
  }

  @PostMapping("/loginIdfind")
  public String Idfind(@ModelAttribute MemberDTO memberDTO, Model model) {
    String findloginID = memberService.findByEmail(memberDTO);
    System.out.println(findloginID);
    model.addAttribute("logId", findloginID);
    return "member/loginIdfindresult";
  }

  @GetMapping("/loginPwfind")
  public String PwfindForm() {
    return "member/loginPwfinder";
  }

  @PostMapping("/loginPWfind")
  public String Pwfind(@ModelAttribute MemberDTO memberDTO) {
    EmailDTO dto = memberService.createMailAndChangePassword(memberDTO.getEmail(), memberDTO.getLoginId());
    if (dto == null) {
      System.out.println("오류 발생!");

      return "redirect:/member/loginPwfind";
    }
    else{
      memberService.mailSend(dto);
      return "member/loginPwfindResult";
    }
  }

  @GetMapping("/test")
  @ResponseBody
  public PrincipalDetails test(@AuthenticationPrincipal PrincipalDetails principalDetails){
    if(principalDetails == null){
      return null;
    }
    return principalDetails;
  }

  @GetMapping("/auth/login")
  public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String exception, Model model) {
      model.addAttribute("errorMessage", exception);
//    model.addAttribute("errorMessage", "테스트 에러 메시지");

    return "member/loginForm";
  }

}
