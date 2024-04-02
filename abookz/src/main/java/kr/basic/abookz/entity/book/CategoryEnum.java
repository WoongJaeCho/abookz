package kr.basic.abookz.entity.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CategoryEnum {
  HUMANITIES(1, "인문"),
  SOCIAL_SCIENCE(2, "사회과학"),
  HISTORY_CULTURE(3, "역사/문화"),
  TRAVEL(4, "여행"),
  YOUTH(5, "청소년"),
  FOREIGN_LANGUAGE(6, "외국어"),
  HEALTH_RELIGION(7, "건강"),
  SCIENCE_ENGINEERING(8, "과학/공학"),
  COMPUTER(9, "컴퓨터"),
  CERTIFICATION(10, "자격증"),
  NOVEL(11, "소설"),
  MANAGEMENT_ECONOMY(12, "경영/경제"),
  SELF_IMPROVEMENT(13, "자기계발"),
  LITERATURE(14, "종교");

  private int code;
  private String categoryName;
}
