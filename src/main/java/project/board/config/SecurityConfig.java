package project.board.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.board.auth.filter.JwtAuthenticationFilter;
import project.board.auth.token.JwtTokenProvider;


@EnableWebSecurity
@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    @Order(1)
    public SecurityFilterChain globalSecurityFilterChain(HttpSecurity http) throws Exception {
        http = applyCommonConfig(http)
                .securityMatcher("/api/auth/**", "/swagger/**", "/health")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/signup", "/api/auth/logout").permitAll()
                        // /me 같은 정보 조회는 인증 필요
                        .requestMatchers("/api/auth/me").authenticated()
                        .anyRequest().permitAll())
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userApiFilterChain(HttpSecurity http) throws Exception{
        http = applyCommonConfig(http)
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/members/**","/api/posts/**").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(e->
                        e
                                .accessDeniedHandler(accessDeniedHandler)
                                .authenticationEntryPoint(authenticationEntryPoint)

                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain viewSecurityFilterChain(HttpSecurity http) throws Exception {
        http = applyCommonConfig(http)
                .securityMatcher("/posts/**", "/members/**") // 뷰 전용 요청만 필터링
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/posts/create").authenticated()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(e -> e
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    private static HttpSecurity applyCommonConfig(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
