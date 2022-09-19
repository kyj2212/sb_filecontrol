package yejin.site.filecontrol.base;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import yejin.site.filecontrol.member.service.MemberUserDetailService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /* 인가 구분을 위한 url path 지정 */
    private static final String[] AUTH_WHITELIST_STATIC = {
            "/css/**",
            "/js/**",
            "/assets/**",
            "/error/**"}; // 정적 파일 인가 없이 모두 허용
    private static final String[] AUTH_ALL_LIST = {
            "/signup",
            "/login"
    }; // 모두 허용
    private static final String[] AUTH_ADMIN_LIST = {
            "/admin/**"
    }; // admin 롤 만 허용
    private static final String[] AUTH_AUTHENTICATED_LIST = {
            "/member/**",
            "/**"
    }; // 인가 필요

    private final MemberUserDetailService customUserDetailsService;
    // private final AuthenticationFailureHandler customFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }

/*
    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return new CustomSuccessHandler("/");
    }
*/

/*    @Bean
    @Order(0)
    SecurityFilterChain resources(HttpSecurity http) throws Exception {
        http
                .requestMatchers((matchers) -> matchers.antMatchers(AUTH_WHITELIST_STATIC))
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll())
                .requestCache().disable()
                .securityContext().disable()
        //              .sessionManagement().disable()
        ;
        return http.build();
    }*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        csrf -> csrf.disable()
                        //.ignoringAntMatchers("/h2-console/**")
                )

                .authorizeRequests(
                        authorizeRequests -> authorizeRequests
                                .antMatchers(AUTH_ALL_LIST).permitAll()
                                .antMatchers(AUTH_AUTHENTICATED_LIST).authenticated()
//                                .antMatchers(AUTH_ADMIN_LIST).hasRole("ADMIN")
                )

                .headers(
                        headers -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login") // get
                                .loginProcessingUrl("/login") //post
                //                .successHandler(customSuccessHandler())
                //                .failureHandler(customFailureHandler);
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling.accessDeniedPage("/restrict")
                );
        return http.build();
    }

}