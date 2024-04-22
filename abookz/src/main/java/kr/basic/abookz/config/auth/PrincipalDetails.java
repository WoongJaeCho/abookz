package kr.basic.abookz.config.auth;

import kr.basic.abookz.entity.member.MemberEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
  private MemberEntity member;
  private Map<String, Object> attributes;

  public PrincipalDetails(MemberEntity member) {
    this.member = member;
  }

  public PrincipalDetails(MemberEntity member, Map<String, Object> attributes) {
    this.member = member;
    this.attributes = attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    collection.add(() -> member.getRole().toString());
    return collection;
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  @Override
  public String getUsername() {
    return member.getLoginId();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true; // 활성화 여부 로직에 따라 조정
  }

  @Override
  public Map<String, Object> getAttribute(String name) {
    return attributes;
  }

  @Override
  public String getName() {
    return member.getName();
  }

  public String getProfile() {
    return member.getProfile();
  }
}
