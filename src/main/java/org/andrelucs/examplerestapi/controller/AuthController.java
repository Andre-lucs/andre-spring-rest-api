package org.andrelucs.examplerestapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.andrelucs.examplerestapi.exceptions.InvalidJwtAuthenticationException;
import org.andrelucs.examplerestapi.model.dto.TokenDTO;
import org.andrelucs.examplerestapi.model.dto.UserLoginDTO;
import org.andrelucs.examplerestapi.service.AuthService;
import org.andrelucs.examplerestapi.util.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication endpoint" , description = "The endpoint user for authentication services.")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping( path = "/login", consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Authenticates a user and return a token")
    public ResponseEntity<TokenDTO> login(@RequestBody UserLoginDTO loginDTO){
        if(loginDTO == null || loginDTO.getUsername() == null || loginDTO.getUsername().isBlank()
        || loginDTO.getPassword() == null || loginDTO.getPassword().isBlank() ){
            throw new InvalidJwtAuthenticationException("Invalid credentials");
        }
        try {
            var token = authService.login(loginDTO);
            return ResponseEntity.ok(token);
        }catch (Exception e){
            throw new InvalidJwtAuthenticationException("Login failed: "+e.getMessage());
        }
    }

    @PutMapping(path = "/refresh/{username}", consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML}, produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Refreshes a token")
    public ResponseEntity<TokenDTO> refresh(@PathVariable("username") String username ,@RequestHeader("Authorization") String refreshToken){
        if(!refreshToken.startsWith("Bearer "))throw new InvalidJwtAuthenticationException("Invalid token format");
        refreshToken = refreshToken.substring(7);
        var newToken = authService.refreshToken(username,refreshToken);
        return ResponseEntity.ok(newToken);
    }
}
