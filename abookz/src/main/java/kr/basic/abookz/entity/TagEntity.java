package kr.basic.abookz.entity;

import lombok.Getter;

@Getter
public enum TagEntity {
  READ("읽은책"),
  WANT_TO_READ("읽고싶은책"),
  CURRENTLY_READING("읽고있는책");

  private final String korean;
  //저장은 영어로 되있지만 get으로 한국어를 읽어옴
  TagEntity(String korean) {
    this.korean = korean;
  }
}
