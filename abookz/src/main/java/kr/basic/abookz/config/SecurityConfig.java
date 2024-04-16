
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
                .requestMatchers("/member/save", "/member/validId", "/member/loginIdfind", "/member/loginPwfind", "/member/loginPWfind").permitAll()
                .requestMatchers("/member/**").authenticated()
                .requestMatchers("/myshelf").authenticated()
                .requestMatchers("/challenge").authenticated()
                .requestMatchers("/manager/**").hasAnyRole("MANAGER","ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                .anyRequest().permitAll()
        ).formLogin(
            form->{
                form.loginPage("/member/loginForm")
                    .loginProcessingUrl("/login")
                    .failureHandler(customAuthFailureHandler())
                    .successHandler(customSuccessHandler)
                    .permitAll();

            }
        ).oauth2Login(

            oauth2 -> oauth2
                .loginPage("/member/loginForm")
                .successHandler(customSuccessHandler)
                .failureHandler(customAuthFailureHandler())
                .permitAll()


        );

        return http.build();
    }
}
