package kr.basic.abookz;

import kr.basic.abookz.dto.ReviewDTO;
import kr.basic.abookz.entity.review.ReviewEntity;
import kr.basic.abookz.repository.ReviewRepository;
import kr.basic.abookz.service.BookShelfService;
import kr.basic.abookz.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@SpringBootTest
@Transactional
public class reviewTest {
  @Autowired
  ReviewService reviewService;
  @Autowired
  ReviewRepository reviewRepository;
  @Autowired
  BookShelfService bookShelfService;
  @Test
  void reviews(){
      PageRequest pageRequest = PageRequest.of(1, 3, Sort.by(Sort.Direction.ASC, "id"));

      Page<ReviewEntity> page = reviewService.getReviewsByBookId(2L, pageRequest);
      Page<ReviewDTO> dtoPage = page.map(r -> new ReviewDTO(r.getId(),r.getContent(),r.getReviewGrade(),r.getCreatedDate(),r.getIsSpoilerActive(),bookShelfService.mapEntityToDTO(r.getBookShelf())));

    List<ReviewDTO> content = dtoPage.getContent(); //조회된 데이터

      int size = content.size();//조회된 데이터 수
      long totalElements = page.getTotalElements();//전체 데이터 수
      int number = page.getNumber();//페이지 번호
      int totalPages = page.getTotalPages();//전체 페이지 번호
      boolean first = page.isFirst();//첫번째 항목인가?
      boolean next = page.hasNext();//다음 페이지가 있는가?

    for (ReviewDTO reviewDTO : content) {
      System.out.println("reviewDTO = " + reviewDTO);
    }
    System.out.println("size = " + size);
    System.out.println("totalElements = " + totalElements);
    System.out.println("number = " + number);
    System.out.println("totalPages = " + totalPages);
    System.out.println("first = " + first);
    System.out.println("next = " + next);
  }
}
