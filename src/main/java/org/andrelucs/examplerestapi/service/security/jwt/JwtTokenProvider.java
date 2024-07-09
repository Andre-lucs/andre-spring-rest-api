package org.andrelucs.examplerestapi.service.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.andrelucs.examplerestapi.exceptions.InvalidJwtAuthenticationException;
import org.andrelucs.examplerestapi.model.dto.TokenDTO;
import org.andrelucs.examplerestapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length:3600000}")
    private Long validityMs;

    @Autowired
    private UserService userService;

    private Algorithm algorithm = null;

    @PostConstruct
    private void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey);
    }

    public TokenDTO createAccessToken(String username, List<String> roles){
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMs);
        String accessToken = getAccessToken(username, roles, now, validity);
        String refreshToken = getRefreshToken(username, roles, now);
        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }


    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerUrl)
                .sign(algorithm)
                .strip();
    }
    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date refreshTokenValidity = new Date(now.getTime() + validityMs * 3);
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenValidity)
                .withSubject(username)
                .sign(algorithm)
                .strip();
    }

    public Authentication getAuthentication(String token){
        DecodedJWT decodedToken = decodeToken(token);
        UserDetails userDetails = this.userService.loadUserByUsername(decodedToken.getSubject());//subject is the username
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            return verifier.verify(token);
        }catch (Exception e){
            throw new InvalidJwtAuthenticationException("Expired or invalid token. "+e.getMessage());
        }
    }

    public String resolveToken(HttpServletRequest req){
        String authorization = req.getHeader("Authorization");

        if(authorization != null && authorization.startsWith("Bearer ")){
            return authorization.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validateToken(String token){
        try{
            var decodedToken = decodeToken(token);
            return !decodedToken.getExpiresAt().before(new Date());
        }catch (Exception e){
            throw new InvalidJwtAuthenticationException("Expired or invalid token.");
        }
    }


    public TokenDTO refreshToken(String refreshToken) {
        var isValid = validateToken(refreshToken);
        if(isValid){
            var decoded = decodeToken(refreshToken);
            return createAccessToken(decoded.getSubject(), decoded.getClaim("roles").asList(String.class));
        }
        return null;
    }
}
