package CY.cymake.Config;

import CY.cymake.Security.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;
    private CustomAccessDeniedHandler accessDeniedHandler;
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/v1/users/register",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/v1/auth/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeHttpRequests ->authorizeHttpRequests
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated())// @PreAuthrization을 사용할 것이기 때문에 모든 경로에 대한 인증처리는 Pass
                .csrf(AbstractHttpConfigurer::disable)// CSRF 보호를 비활성화 (개발 중에만 사용)
                .cors(Customizer.withDefaults())
                .headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin).disable())
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS
                )) //세션 관리 상태 없음으로 구성. Spring Security가 세션 생성 or 사용 X
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP 기본 인증 비활성화
                .addFilterBefore(new JwtAuthFilter(customUserDetailService, jwtUtil), UsernamePasswordAuthenticationFilter.class) //JwtAuthFilter를 usernamepasswordauthenticationfilter앞에 추가
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }
}

