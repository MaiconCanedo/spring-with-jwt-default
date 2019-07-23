package br.com.maicon.pratica.jwt.security;

import br.com.maicon.pratica.jwt.security.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Optional.ofNullable(request.getHeader(JWTUtil.HEADER_AUTHORIZATION))
                .filter(header -> header.startsWith(JWTUtil.HEADER_AUTHORIZATION_PREFIX))
                .map(token -> token.replaceAll(JWTUtil.HEADER_AUTHORIZATION_PREFIX, ""))
                .ifPresent(token -> getAuthentication(token).ifPresent(auth ->
                        SecurityContextHolder.getContext().setAuthentication(auth)));
        chain.doFilter(request, response);
    }

    private Optional<UsernamePasswordAuthenticationToken> getAuthentication(String token) {
        return jwtUtil.validarToken(token).map(username -> {
            final UserDetails user = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        });
    }
}
