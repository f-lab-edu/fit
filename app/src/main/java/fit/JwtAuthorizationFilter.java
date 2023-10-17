package fit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final MemberRepository repository;
    private final JwtConfig jwtConfig;

    public JwtAuthorizationFilter(MemberRepository repository, JwtConfig jwtConfig) {
        this.repository = repository;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);

        // token 여부
        if (authentication == null || !authentication.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // token 추출
        String token = authentication.split(" ")[1];

        // token 만료 여부
        if (jwtConfig.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 이메일 추출
        String email = jwtConfig.getEmail(token);

        // 이메일 체크
        Optional<Member> member = repository.findMemberByEmail(email);
        if (member.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
                null,
                null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}