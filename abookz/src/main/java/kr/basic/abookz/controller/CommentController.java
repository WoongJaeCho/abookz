package kr.basic.abookz.controller;

import kr.basic.abookz.config.auth.PrincipalDetails;
import kr.basic.abookz.dto.BookDTO;
import kr.basic.abookz.dto.BookShelfDTO;
import kr.basic.abookz.dto.RatingDTO;
import kr.basic.abookz.dto.ReviewDTO;
import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
  private final ReviewService reviewService;
  private final MemberService memberService;
  private final BookService bookService;
  private final BookShelfService shelfService;
  private final LikeService likeService;

  @GetMapping("")
  public String comment(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {

    return "review/comment";
  }


}
