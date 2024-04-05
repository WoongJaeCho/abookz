package kr.basic.abookz.repository;

import kr.basic.abookz.entity.book.BookEntity;
import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.entity.book.TagEnum;
import kr.basic.abookz.entity.member.MemberEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.List;

import static kr.basic.abookz.entity.book.TagEnum.CURRENTLY_READING;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class BookShelfRepositoryTest {
  @Autowired
  BookShelfRepository bsr;
  @Autowired
  MemberRepository mr;
  @Autowired
  BookRepository br;


  @Test
  public void testFind() {
    MemberEntity testMember = new MemberEntity("test");
    mr.save(testMember);
    BookEntity testBook = new BookEntity("testBook");
    br.save(testBook);
    BookShelfEntity testBookShelf = new BookShelfEntity(testMember, testBook, CURRENTLY_READING);
    bsr.save(testBookShelf);

    List<BookShelfEntity> findBookShelfs = bsr.findAllByMemberIdAndTag(1L, CURRENTLY_READING);
    String korean = testBookShelf.getTag().getKorean();
    System.out.println("korean = " + korean);
    assertThat(findBookShelfs.get(0).getBook().getTitle()).isEqualTo(testBookShelf.getBook().getTitle());
  }
}