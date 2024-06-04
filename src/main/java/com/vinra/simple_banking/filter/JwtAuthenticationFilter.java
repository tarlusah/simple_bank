package com.vinra.simple_banking.filter;

import com.vinra.simple_banking.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            filterChain.doFilter(request, response);
            return;
        }

        logger.debug("Authentication header: " + authHeader);
        if (authHeader==null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Header : " + authHeader);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);

        if (jwtUtils.validateJwtToken(jwt)){
            userEmail = jwtUtils.getUserNameFromJwt(jwt);
            System.out.println("username: " + userEmail);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication Success");
//                System.out.println("userDetails: " + userDetails);
                request.setAttribute("userDetails", userDetails);
                filterChain.doFilter(request, response);
            }else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                filterChain.doFilter(request, response);
                return;
            }
        }else{
            System.out.println("Invalid JWT token");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            filterChain.doFilter(request, response);
            return;
        }

    }
}
