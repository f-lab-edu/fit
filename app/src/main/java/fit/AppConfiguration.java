package fit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public SignupCommandExecutor signupCommandExecutor(MemberRepository memberRepository) {
        return new SignupCommandExecutor(memberRepository);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(JwtConfig jwtConfig) {
        return new JwtAuthorizationFilter(jwtConfig);
    }

    @Bean
    public JwtConfig jwtConfig(@Value("${jwt.secret.key}") String jwtSecretKey,
                               @Value("${jwt.expiry.time}") long jwtExpiryTime) {
        return new JwtConfig(jwtSecretKey, jwtExpiryTime);
    }
}
