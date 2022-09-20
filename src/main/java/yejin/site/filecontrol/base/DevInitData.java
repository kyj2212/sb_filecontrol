package yejin.site.filecontrol.base;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import yejin.site.filecontrol.base.exception.SignupEmailDuplicatedException;
import yejin.site.filecontrol.base.exception.SignupUsernameDuplicatedException;
import yejin.site.filecontrol.member.service.MemberService;

@Configuration
@Profile("dev") // 이 클래스 정의된 Bean 들은 test 모드에서만 활성화 된다.
public class DevInitData {
    // CommandLineRunner : 주로 앱 실행 직후 초기데이터 세팅 및 초기화에 사용
    @Bean
    CommandLineRunner init(MemberService memberService, PasswordEncoder passwordEncoder) {
        return args -> {
            String password = passwordEncoder.encode("1234");
            try {
                memberService.create("user1", "1234","유저1", "user1@test.com");

                memberService.create("user2", "1234", "유저2","user2@test.com");
            } catch (SignupUsernameDuplicatedException e) {
                throw new RuntimeException(e);
            } catch (SignupEmailDuplicatedException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
