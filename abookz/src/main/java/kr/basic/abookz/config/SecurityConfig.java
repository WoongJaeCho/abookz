package kr.basic.abookz.config;

import kr.basic.abookz.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@EnableWebSecurity  // 우리 웹 필터에 시큐리티 필터를 적용해줌
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomSuccessHandler customSuccessHandler;
    private final PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)->{
            web.ignoring().requestMatchers(new String[]{"/favicon.ico","/resources/**","/error"});
        };
    }

    @Bean
    AuthenticationFailureHandler customAuthFailureHandler(){
        return new CustomAuthFailureHandler();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                authz -> authz
                        .requestMatchers("/user/**").authenticated() // 인증이 되면 누구나 들어올수있음
                        .requestMatchers("/myshelf/**").authenticated()
                        .requestMatchers("/bestSeller/**").authenticated()
                        .requestMatchers("/search/**").authenticated()
                        .requestMatchers("/readingUpdate/**").authenticated()
                        .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN") // role이 메니저나 어드민만 들어올수있음
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                        .anyRequest().permitAll()
        ).formLogin(
                form->{
                    form.loginPage("/member/login")   // 우리가 만든 로그인페이지로 자동 인터셉트됨
                        .loginProcessingUrl("/login")
                            .failureHandler(customAuthFailureHandler())
                            .successForwardUrl("/test");
                }
        ).oauth2Login(
                oauth2 -> oauth2
                        .loginPage("/")
                        .successHandler(customSuccessHandler)
                        .failureHandler(customAuthFailureHandler())
                        .permitAll()

        );

        return http.build();
    }
}
