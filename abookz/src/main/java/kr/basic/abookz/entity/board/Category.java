package kr.basic.abookz.entity.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public enum Category {
  FREE("자유"),
  QUESTION("질문"),
  NOTICE("공지"),
  EVENT("이벤트");

  private final String korean;

  Category(String korean){
    this.korean = korean;
  }
}
