package fit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtAuthorizationFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {
        if (request == null || filterChain == null) {
            return;
        }

        final String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authentication == null || !authentication.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(authentication);

        if (jwtConfig.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        MemberView memberView = jwtConfig.getMemberView(token);
        authenticate(request, memberView);

        filterChain.doFilter(request, response);
    }

    private static void authenticate(HttpServletRequest request, MemberView memberView) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(memberView, null, null);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private static String extractToken(String authentication) {
        return authentication.split(" ")[1];
    }
}