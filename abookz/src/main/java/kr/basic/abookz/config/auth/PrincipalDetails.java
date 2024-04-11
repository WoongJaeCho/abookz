package kr.basic.abookz.config.auth;

import kr.basic.abookz.entity.member.MemberEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 /login 주소가 오면 낚아서 로그인 진행
// login 완료 -> security session 을 만든다 (Security ContextHolder)
// type --> authentication type object
// authentication 객체 --> user 정보를 넣어야함 => userDetails

@Data
public class PrincipalDetails implements UserDetails , OAuth2User{

  private MemberEntity member;
  private Map<String, Object> attributes;

  // 일반 로그인 객체
  public PrincipalDetails(MemberEntity member){
    this.member = member;
  }
  // OAuth2.0 로그인시 사용
  public PrincipalDetails(MemberEntity member, Map<String, Object> attributes){
    this.attributes = attributes; // 구글 로그인할때 프로필 정보 이메일이 넘겨옴
    this.member = member;
  }


  // user 권한 넘겨준다
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Collection<GrantedAuthority> collection = new ArrayList<>();
    collection.add(new GrantedAuthority() {
      @Override
      public String getAuthority() {
        return member.getRole().toString();
      }
    });
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

  // 계정이 만료되지않았는가?
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  // 계정이 잠가지지 않았나?
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  // user 비번이 기간이 지났나?
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  // 계정이 활성화 되어있는가?
  @Override
  public boolean isEnabled() {
    // 계정이 비활성화 될때 : 1년동안 방문하지 않는 사이트 -> 휴면계정
    return true;
  }

  @Override
  public Map<String, Object> getAttribute(String name) {
    return attributes;
  }

  @Override
  public String getName() {
    return member.getName();
  }
}