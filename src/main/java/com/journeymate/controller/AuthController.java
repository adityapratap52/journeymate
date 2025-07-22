package com.journeymate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.journeymate.config.JwtTokenProvider;
import com.journeymate.dto.AuthRequest;
import com.journeymate.dto.JwtResponse;
import com.journeymate.dto.PasswordResetRequest;
import com.journeymate.dto.UserDTO;
import com.journeymate.service.ContextService;
import com.journeymate.service.UserService;
import com.journeymate.utils.ITag;
import com.journeymate.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = ITag.AUTH_TAG_NAME, description = ITag.AUTH_TAG_DESCRIPTION)
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider tokenProvider;
	private final UserService userService;

	private final ContextService contextService;

	@Operation(summary = ITag.AUTH_LOGIN_SUMMARY, description = ITag.AUTH_LOGIN_DESCRIPTION, responses = {
			@ApiResponse(responseCode = "200", description = ITag.AUTH_LOGIN_200_DESCRIPTION, content = @Content(schema = @Schema(implementation = JwtResponse.class))),
			@ApiResponse(responseCode = "401", description = ITag.AUTH_LOGIN_401_DESCRIPTION) })
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken(authentication.getName());

		return ResponseEntity.ok(new JwtResponse(jwt));
	}

	@Operation(summary = ITag.AUTH_REGISTER_SUMMARY, description = ITag.AUTH_REGISTER_DESCRIPTION, responses = {
			@ApiResponse(responseCode = "200", description = ITag.AUTH_REGISTER_200_DESCRIPTION, content = @Content(schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(responseCode = "400", description = ITag.AUTH_REGISTER_400_DESCRIPTION) })
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
		return ResponseEntity.ok(userService.createUser(userDTO));
	}

	@Operation(summary = "Reset password", description = "Resets user's password using old and new password", responses = {
			@ApiResponse(responseCode = "200", description = "Password reset successful"),
			@ApiResponse(responseCode = "400", description = "Invalid input or incorrect old password"),
			@ApiResponse(responseCode = "404", description = "User not found") })
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
		userService.resetPassword(request);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Get current user", description = "Get current logged in user", responses = {
			@ApiResponse(responseCode = "200", description = "Successfully retrieved Current User", content = @Content(schema = @Schema(implementation = UserDTO.class))) })
	@GetMapping("current-user")
	public ResponseEntity<UserDTO> getAllTripPosts() {
		return ResponseEntity.ok(UserDTO.convertToDTO(contextService.getCurrentUser()));
	}
	
	@Operation(summary = "Update User Profile Image", description = "Updates an existing image", responses = {
			@ApiResponse(responseCode = "200", description = "Profile Image updated successfully", content = @Content(schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "403", description = "Access forbidden"),
			@ApiResponse(responseCode = "404", description = "User not found") })
	@PostMapping(value = "updateProfileImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("@contextService.getCurrentUserId() != null")
	public ResponseEntity<?> updateProfileImage(@RequestPart(value = "image", required = false) MultipartFile image) {
		return new ResponseEntity<>(userService.updateProfileImage(image), HttpStatus.OK);
	}
}