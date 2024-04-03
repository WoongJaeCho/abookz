package kr.basic.abookz.service;

import kr.basic.abookz.entity.book.BookShelfEntity;
import kr.basic.abookz.repository.BookShelfRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookShelfService {

    private final BookShelfRepository bookShelfRepository;

    public BookShelfEntity save(BookShelfEntity bookShelfEntity){
        return bookShelfRepository.save(bookShelfEntity);
    }
    public List<BookShelfEntity> findAllByMemberId(Long memId){

       
            return bookShelfRepository.findAllByMemberId(memId);
    }
}
