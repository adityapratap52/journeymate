	package com.journeymate.controller.user;

import com.journeymate.dto.UserDTO;
import com.journeymate.model.user.User;
import com.journeymate.service.ContextService;
import com.journeymate.service.UserService;
import com.journeymate.utils.ITag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = ITag.USER_TAG_NAME, description = ITag.USER_TAG_DESCRIPTION)
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ContextService contextService;

    @Operation(
        summary = ITag.USER_GET_ALL_SUMMARY,
        description = ITag.USER_GET_ALL_DESCRIPTION,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = ITag.API_RESPONSE_200,
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = ITag.API_RESPONSE_403
            )
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
        summary = ITag.USER_GET_BY_ID_SUMMARY,
        description = ITag.USER_GET_BY_ID_DESCRIPTION,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = ITag.API_RESPONSE_200,
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = ITag.API_RESPONSE_404
            ),
            @ApiResponse(
                responseCode = "403",
                description = ITag.API_RESPONSE_403
            )
        }
    )
    @GetMapping("/getUserByUid/{uid}")
    @PreAuthorize("hasRole('ADMIN') or authentication.name != null")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String uid) {
        return new ResponseEntity<>(userService.getUserByUid(uid), HttpStatus.OK);
    }

    @Operation(
        summary = ITag.USER_UPDATE_SUMMARY,
        description = ITag.USER_UPDATE_DESCRIPTION,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = ITag.API_RESPONSE_200,
                content = @Content(schema = @Schema(implementation = User.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = ITag.API_RESPONSE_400
            ),
            @ApiResponse(
                responseCode = "404",
                description = ITag.API_RESPONSE_404
            ),
            @ApiResponse(
                responseCode = "403",
                description = ITag.API_RESPONSE_403
            )
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#id).get().username == authentication.name")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return ResponseEntity.ok(userService.updateUser(id, userDetails));
    }

    @Operation(
        summary = ITag.USER_DELETE_SUMMARY,
        description = ITag.USER_DELETE_DESCRIPTION,
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = ITag.API_RESPONSE_200
            ),
            @ApiResponse(
                responseCode = "403",
                description = ITag.API_RESPONSE_403
            ),
            @ApiResponse(
                responseCode = "404",
                description = ITag.API_RESPONSE_404
            )
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}