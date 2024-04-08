package kr.basic.abookz.entity.admin;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="slide_card")
public class SlideCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slideFileName; // 슬라이드 파일 이름
    private Long ISBN13; // 해당 도서 ISBN13 -> 해당 도서 URL 이동
    private LocalDate addDate; // 파일 등록 날짜
    private int idx; // 슬라이드 순서, 0: 비활성화

}
