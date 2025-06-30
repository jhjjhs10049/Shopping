package org.zerock.ex3.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zerock.ex3.member.security.filter.JWTCheckFilter;

import java.security.Security;
import java.util.List;

@Configuration
@Log4j2
@EnableMethodSecurity//(prePostEnabled = true)
//prePostEnabled : @PreAutorize 와 PostAutorize 둘다 사용가능
//@PreAutorize : 메서드가 실행되기전에 인증을 거친다
//@PostAutorize : 메서드가 실행되고 응답을 보내기 전에 인증을 거친다.
public class CustomSecurityConfig {

    private JWTCheckFilter jwtCheckFilter;

    @Autowired
    private void setJwtCheckFilter(JWTCheckFilter jwtCheckFilter) {
        this.jwtCheckFilter = jwtCheckFilter;
    }    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("filter chain................");

        httpSecurity.formLogin(httpSecurityFormLoginConfigurer -> {
            httpSecurityFormLoginConfigurer.disable();
        });

        httpSecurity.logout( config -> config.disable());

        httpSecurity.csrf(config -> { config.disable();});

        httpSecurity.sessionManagement(sessionManagementConfigurer -> {
            sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.NEVER);
        });        // 인증이 필요없는 URL들 설정 - 순서가 중요함
        httpSecurity.authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/v1/token/**").permitAll()  // 로그인, 토큰 갱신
            .requestMatchers("/api/v1/member/register").permitAll()  // 회원가입만 허용
            .requestMatchers("/upload/**").permitAll() // 이미지 업로드 파일 접근 허용
            .anyRequest().authenticated()  // 나머지는 인증 필요
        );

        httpSecurity.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.cors(cors -> {
            cors.configurationSource(corsConfigurationSource());
        });

        return httpSecurity.build();
    }

    @Bean
    //게임 실행시에 미리 준비해놓는 설정처럼 스프링부트내에서 관리하며 실행전에 미리 준비되는 설정
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOriginPatterns(List.of("*")); //어디서든 허락
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

}
