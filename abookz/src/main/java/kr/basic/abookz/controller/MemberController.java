package kr.basic.abookz.controller;

import jakarta.servlet.http.HttpSession;
import kr.basic.abookz.dto.MemberDTO;
import kr.basic.abookz.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
  // 생성자 주입
  private final MemberService memberService;

  // 조회
  @GetMapping("/list")
  public String findAll(Model model){
    List<MemberDTO> list = memberService.findAll();
    model.addAttribute("memberList", list);
    return "list";
  }
  @GetMapping("/{id}")
  public String findById(@PathVariable Long id, Model model){
    MemberDTO memberDTO = memberService.findById(id);
    model.addAttribute("member", memberDTO);
    return "detail";
  }

  // 가입
  @GetMapping("/save")
  public String saveForm(){
    return "save";
  }
  @PostMapping("/save")
  public String saveMember(@ModelAttribute MemberDTO memberDTO){
    System.out.println("memberDTO = " + memberDTO);
    memberService.save(memberDTO);
    return "test";
  }

  // 로그인
  @GetMapping("/login")
  public String loginForm(){
    return "login";
  }
  @PostMapping("/login")
  public String loginMember(@ModelAttribute MemberDTO memberDTO, HttpSession session){
    MemberDTO loginResult = memberService.login(memberDTO);
    if(loginResult != null){
      // 로그인 성공시
      session.setAttribute("loginId", loginResult.getLoginId());
      return "redirect:/";
    }
    else {
      // 로그인 실패시
      return "login";
    }
  }

  // 수정
  @GetMapping("/update")
  public String updateForm(HttpSession session, Model model){
    String getId = (String)session.getAttribute("loginId");
    MemberDTO memberDTO = memberService.updateForm(getId);
    model.addAttribute("updateMember", memberDTO);
    return "update";
  }
  @PostMapping("/update")
  public String update(@ModelAttribute MemberDTO memberDTO){
    memberService.update(memberDTO);
    return "redirect:/member/" + memberDTO.getId();
  }

  // 삭제
  @GetMapping("/delete/{id}")
  public String deleteById(@PathVariable Long id){
    memberService.deleteById(id);
    return "redirect:/member/list";
  }

  // 로그아웃
  @GetMapping("/logout")
  public String logout(HttpSession session){
    session.invalidate();
    return "redirect:/";
  }
}
