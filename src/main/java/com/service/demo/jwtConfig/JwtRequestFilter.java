package com.service.demo.jwtConfig;

import com.service.demo.jwtBlacklistService.JwtBlackListService;
import com.service.demo.model.User;
import com.service.demo.repo.UserRepo;
import com.service.demo.tokenHandling.JwtUtill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtill jwtUtill;
    @Autowired
    private UserRepo repo;
    @Autowired
    private JwtBlackListService jwtBlackListService;


   private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");
        if(tokenHeader!=null && tokenHeader.startsWith("Bearer ") ) {
            String token = tokenHeader.substring(7);

            if (!jwtBlackListService.isTokenBlacklisted(token)) {

                logger.info("token" + token);
                String userName = jwtUtill.extractToken(token);
                Optional<User> user = repo.findByName(userName);
                if (user.isPresent()) {

                    User user1 = user.get();

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                            (user1, null, Collections.singletonList(new SimpleGrantedAuthority(user1.getRole())));

                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }

            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"You are already logged out, please login again!\"}");
                return;

            }

        }
        filterChain.doFilter(request,response);

    }
}
