package kr.basic.abookz.dto.admin;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideCardDTO {

    private Long id;
    private String slideFileName; // 슬라이드 파일 이름
    private Long ISBN13; // 해당 도서 ISBN13 -> 해당 도서 URL 이동
    private LocalDate addDate; // 파일 등록 날짜
    private int idx; // 슬라이드 순서, 0: 비활성화

    @Override
    public String toString() {
        return "SlideCardDTO{" +
                "slideFileName='" + slideFileName + '\'' +
                ", ISBN13=" + ISBN13 +
                ", addDate=" + addDate +
                ", idx=" + idx +
                '}';
    }
}
