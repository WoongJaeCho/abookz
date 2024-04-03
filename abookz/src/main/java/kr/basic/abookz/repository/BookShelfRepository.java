package kr.basic.abookz.repository;

import kr.basic.abookz.entity.book.BookShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BookShelfRepository extends JpaRepository<BookShelfEntity,Long> {

}
