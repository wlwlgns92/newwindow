package ansan.config;

import ansan.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity // 시큐리티
@Configuration // 설정 클래스로 설정하는 어노테이션
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 시큐리티 설정 연결 하는 클래스 상속
    // 스프링 시큐리티 [ 보안 솔루션 ]
        // 1. 웹 페이지 접근시 로그인 페이지 실행 [ 콘솔에 password 확인 ]
            // Override 단축키 alt + insert
        // 2. 읽기 쓰기 삭제 불가 => csrf [ 사이트간 요청 위조 ]
    @Autowired
    public PasswordEncoder passwordEncoder() { // 패스워드 암호화 관련 클래스
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception { // 웹 리소스 접근 보안
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 웹 URL 접근 보안
        http.authorizeHttpRequests() // URL 인증요청 설정
                .antMatchers("/admin/**").hasRole("ADMIN") // .antMatchers("URL").hasRole("권한명"); [ 권한이 ADMIN이면 접근 가능 ]
                .antMatchers("/member/info").hasRole("MEMBER") // info페이지는 권한이 MEMBER 인 경우에만 접근 가능
                .antMatchers("/**").permitAll() // .antMatchers('URL').permitAll() : 모든 권한이 접근 가능
                .and()
                    .csrf() // 사이트 간 요청 위조 설정
                    .ignoringAntMatchers("/**") // ignoringAntMatchers("URL") : 요청위조 보안을 제외 할 URL
                .and()
                    .formLogin() // 로그인 페이지 보안 설정
                    .loginPage("/member/login") // 아이디 비밀번호 입력받을 페이지 URL
                    .loginProcessingUrl("/member/logincontroller") // 로그인 처리할 URL
                    .defaultSuccessUrl("/")
                    .usernameParameter("mid") // 시큐리티 로그인 [ 아이디 ] 기본값은 : username , username이 없으니 mid로 사용
                    .passwordParameter("m_password") // 시큐리티 로그인 [ 패스워드 ] 기본값은 : password, password가 없으니 m_password로 사용
                .and()
                    .logout() // 로그아웃 관련 설정
                    .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout") ) // 로그아웃 URL 설정
                    .logoutSuccessUrl("/") // 로그아웃 성공시
                    .invalidateHttpSession(true)// 세션 초기화
                .and()
                    .exceptionHandling() // 예외 [오류] 페이지 설정
                    .accessDeniedPage("/error"); // 오류 발생시 -> 오류페이지 URL
    }
    @Autowired
    private MemberService memberService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증 관련 보안안
       auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());

    }
}
