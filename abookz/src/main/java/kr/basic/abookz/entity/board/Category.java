package kr.basic.abookz.entity.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public enum Category {
  FREE("자유"),
  QUESTION("질문");

  private final String korean;

  Category(String korean){
    this.korean = korean;
  }
}
