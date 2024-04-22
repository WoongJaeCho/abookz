package kr.basic.abookz.entity.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
//@AllArgsConstructor
public enum RoleEnum {
  ROLE_USER("user"),ROLE_ADMIN("admin"),ROLE_MANAGER("manager");
  private final String Role;
  RoleEnum(String role){
    this.Role = role;
  }
}
