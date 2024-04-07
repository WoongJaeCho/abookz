package kr.basic.abookz.repository.admin;

import kr.basic.abookz.entity.admin.SlideCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlideCardRepository extends JpaRepository<SlideCardEntity, Long> {

//    @Query("SELECT s FROM slide_card s WHERE s.idx > 0 ORDER BY s.idx ASC")
    public List<SlideCardEntity> findTop3ByOrderByIdx();


}
