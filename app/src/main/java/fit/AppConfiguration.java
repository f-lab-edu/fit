package fit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public SignupCommandExecutor signupCommandExecutor(MemberRepository memberRepository) {
        return new SignupCommandExecutor(memberRepository);
    }
}
