package kr.basic.abookz.config.auth;

import kr.basic.abookz.entity.member.MemberEntity;
import kr.basic.abookz.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// /login 자동 UserDetailsService 타입으로 IoC loadUserByUserName();
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //Security session = Authentication = UserDetails
    //session(내부 Authenticaiton (내부 UserDetails ))

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<MemberEntity> member = memberRepository.findByLoginId(loginId);
        System.out.println("member = " + member);
        if(member.isPresent()){
            System.out.println(" 유저 디테일 객체 생성 !!! " + member.get());
            return new PrincipalDetails(member.get()); // 이 함수가 종료가 될때 @Authentication 객체가 만들어진다
        }
        return null;
    }
}
