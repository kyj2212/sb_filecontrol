package yejin.site.filecontrol.base;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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
            "/**"
    }; // 모두 허용
    private static final String[] AUTH_ADMIN_LIST = {
            "/admin/**"
    }; // admin 롤 만 허용
    private static final String[] AUTH_AUTHENTICATED_LIST = {

    }; // 인가 필요

/*
    private final MemberUserDetailService customUserDetailsService;
    // private final AuthenticationFailureHandler customFailureHandler;
*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

/*
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }
*/

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
                .authorizeRequests()
                .antMatchers(AUTH_ALL_LIST).permitAll()
//                .antMatchers(AUTH_ADMIN_LIST).hasRole("ADMIN")
//                .antMatchers(AUTH_AUTHENTICATED_LIST).authenticated()
        ;
        http
                .csrf().disable();
                //.csrf().ignoringAntMatchers("/h2-console/**");

        http
                .headers()
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
        http
                .formLogin()
                .loginPage("/login");
//                .successHandler(customSuccessHandler())
//                .failureHandler(customFailureHandler);
        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
        http
                .exceptionHandling()
                .accessDeniedPage("/restrict");
        return http.build();
    }

}