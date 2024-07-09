package org.andrelucs.examplerestapi.service.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.andrelucs.examplerestapi.exceptions.InvalidJwtAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            var token = jwtTokenProvider.resolveToken((HttpServletRequest) servletRequest);
            boolean isValid = token != null && jwtTokenProvider.validateToken(token);
            if(isValid){
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                if(auth.isAuthenticated()){
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }catch (Exception e){
            throw new InvalidJwtAuthenticationException("Invalid JWT token: "+e.getMessage());
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
