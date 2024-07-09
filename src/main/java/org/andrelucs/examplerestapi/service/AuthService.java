package org.andrelucs.examplerestapi.service;

import org.andrelucs.examplerestapi.exceptions.InvalidJwtAuthenticationException;
import org.andrelucs.examplerestapi.model.dto.TokenDTO;
import org.andrelucs.examplerestapi.model.dto.UserLoginDTO;
import org.andrelucs.examplerestapi.repository.UserRepository;
import org.andrelucs.examplerestapi.service.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManagerBean;

    @Autowired
    private UserRepository userRepository;


    public TokenDTO login(UserLoginDTO userLogin){
        try{
            var username = userLogin.getUsername();
            var password = userLogin.getPassword();
            //password = passwordEncoder.encode(password);
            authenticationManagerBean.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = userRepository.findByUsername(username);
            var tokenResponse = new TokenDTO();

            if(user != null){
                tokenResponse = jwtTokenProvider.createAccessToken(username, user.getRoles());
                return tokenResponse;
            }
            throw new UsernameNotFoundException("Username " + username + " was not found.");
        }catch (Exception e){
            throw new BadCredentialsException("Invalid username or password provided.");
        }
    }

    public TokenDTO refreshToken(String username, String refreshToken){
        var user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        var token = jwtTokenProvider.refreshToken(refreshToken);
        if (token != null) {
            return token;
        }
        throw new InvalidJwtAuthenticationException("Invalid refresh token");
    }
}
