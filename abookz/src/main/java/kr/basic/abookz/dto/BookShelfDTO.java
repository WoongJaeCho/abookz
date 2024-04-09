package kr.basic.abookz.dto;

import kr.basic.abookz.entity.book.TagEnum;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookShelfDTO {
    private Long id;//pk
    @CreationTimestamp
    private LocalDateTime addDate;//등록날짜
    private LocalDateTime startDate;//읽기시작한날짜
    private LocalDateTime endDate;//다읽은날짜
    private LocalDateTime targetDate;//목표날짜
    private int dailyPage;//하루목표 페이지
    private int currentPage; //현재읽은페이지
    private int bookShelfGrade;//평점(1,2,3,4,5) 별표시
    private TagEnum tag; // READ,WANT_TO_READ,CURRENTLY_READING
    private BookDTO bookDTO;
    private MemberDTO memberDTO;

    private Long days;

    @Override
    public String toString() {
        return "BookShelfDTO{" +
                "id=" + id +
                ", addDate=" + addDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", targetDate=" + targetDate +
                ", dailyPage=" + dailyPage +
                ", currentPage=" + currentPage +
                ", bookShelfGrade=" + bookShelfGrade +
                ", tag=" + tag +
                ", bookDTO=" + bookDTO +
                ", memberDTO=" + memberDTO +
                '}';
    }
}
