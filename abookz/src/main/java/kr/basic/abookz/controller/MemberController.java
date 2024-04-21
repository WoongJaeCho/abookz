package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.EmailDTO;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
  public String findAll(Model model) {
    List<MemberDTO> list = memberService.findAll();

    model.addAttribute("memberList", list);
    return "member/list";
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
    System.out.println("id = " + id);
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
  public String changeRole(@ModelAttribute MemberDTO memberDTO){
    System.out.println("memberDto = " + memberDTO);
    memberService.updateRole(memberDTO);
    return "confirm";
  }

  // 삭제
  @GetMapping("/delete/{id}")
  public String deleteById(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long id) {
    if (!logincheck(principalDetails)) {
      return "member/loginForm";
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
  public PrincipalDetails test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    if (principalDetails == null) {
      return null;
    }
    return principalDetails;
  }


  @GetMapping("/auth/login")
  public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String exception, Model model) {
      model.addAttribute("errorMessage", "로그인 중 문제가 발생했습니다: " + exception);
    return "member/loginForm";
  }

}
