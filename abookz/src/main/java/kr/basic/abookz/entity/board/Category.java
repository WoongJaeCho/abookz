package kr.basic.abookz.entity.board;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
  Free("자유"),
  Question("질문");
  private final String cate;
}
