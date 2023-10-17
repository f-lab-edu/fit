package fit;

import fit.executor.DeleteAccountExecutor;
import fit.executor.SignupCommandExecutor;
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
    public DeleteAccountExecutor deleteAccountExecutor(MemberRepository memberRepository) {
        return new DeleteAccountExecutor(memberRepository);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter(MemberRepository memberRepository, JwtConfig jwtConfig) {
        return new JwtAuthorizationFilter(memberRepository, jwtConfig);
    }

    @Bean
    public JwtConfig jwtConfig(@Value("${jwt.secret.key}") String jwtSecretKey,
                               @Value("${jwt.expiry.time}") long jwtExpiryTime) {
        return new JwtConfig(jwtSecretKey, jwtExpiryTime);
    }
}
