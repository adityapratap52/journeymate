package com.journeymate.controller;

import com.journeymate.config.JwtTokenProvider;
import com.journeymate.dto.AuthRequest;
import com.journeymate.dto.JwtResponse;
import com.journeymate.model.user.User;
import com.journeymate.service.UserService;
import com.journeymate.utils.ITag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = ITag.AUTH_TAG_NAME, description = ITag.AUTH_TAG_DESCRIPTION)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                        JwtTokenProvider tokenProvider,
                        UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Operation(
        summary = ITag.AUTH_LOGIN_SUMMARY,
        description = ITag.AUTH_LOGIN_DESCRIPTION,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = ITag.AUTH_LOGIN_200_DESCRIPTION,
                content = @Content(schema = @Schema(implementation = JwtResponse.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = ITag.AUTH_LOGIN_401_DESCRIPTION
            )
        }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication.getName());
        
        return ResponseEntity.ok(new JwtResponse(jwt, loginRequest.getUsername()));
    }

    @Operation(
        summary = ITag.AUTH_REGISTER_SUMMARY,
        description = ITag.AUTH_REGISTER_DESCRIPTION,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = ITag.AUTH_REGISTER_200_DESCRIPTION,
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = ITag.AUTH_REGISTER_400_DESCRIPTION
            )
        }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
}