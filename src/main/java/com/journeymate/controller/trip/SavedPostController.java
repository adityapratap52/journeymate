package com.journeymate.controller.trip;

import com.journeymate.dto.SavedPostDTO;
import com.journeymate.service.SavedPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-posts")
@Tag(name = "Saved Posts", description = "Saved post management APIs")
@SecurityRequirement(name = "bearerAuth")
public class SavedPostController {
    
    private final SavedPostService savedPostService;

    public SavedPostController(SavedPostService savedPostService) {
        this.savedPostService = savedPostService;
    }

    @Operation(
        summary = "Save a post",
        description = "Saves a trip post for a user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Post saved successfully",
                    content = @Content(schema = @Schema(implementation = SavedPostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Post already saved or invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @PostMapping
    @PreAuthorize("isAuthenticated() and #savedPostDTO.userId == authentication.principal.username")
    public ResponseEntity<SavedPostDTO> savePost(@RequestBody SavedPostDTO savedPostDTO) {
        return ResponseEntity.ok(savedPostService.savePost(savedPostDTO));
    }

    @Operation(
        summary = "Get all saved posts",
        description = "Retrieves all saved posts (Admin only)",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved saved posts"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SavedPostDTO>> getAllSavedPosts() {
        return ResponseEntity.ok(savedPostService.getAllSavedPosts());
    }

    @Operation(
        summary = "Get saved posts by user ID",
        description = "Retrieves all posts saved by a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved saved posts"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<List<SavedPostDTO>> getSavedPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(savedPostService.getSavedPostsByUserId(userId));
    }

    @Operation(
        summary = "Get users who saved a post",
        description = "Retrieves all saved post entries for a specific trip post",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved saved posts"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/trip/{tripPostId}")
    @PreAuthorize("hasRole('ADMIN') or @tripService.getTripPostById(#tripPostId).userId == authentication.principal.username")
    public ResponseEntity<List<SavedPostDTO>> getSavedPostsByTripPostId(@PathVariable Long tripPostId) {
        return ResponseEntity.ok(savedPostService.getSavedPostsByTripPostId(tripPostId));
    }

    @Operation(
        summary = "Check if post is saved",
        description = "Checks if a post is saved by a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully checked saved status"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/check/{userId}/{tripPostId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<Boolean> isPostSavedByUser(@PathVariable Long userId, @PathVariable Long tripPostId) {
        return ResponseEntity.ok(savedPostService.isPostSavedByUser(userId, tripPostId));
    }

    @Operation(
        summary = "Unsave a post",
        description = "Removes a saved post for a user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Post unsaved successfully"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Saved post not found")
        }
    )
    @DeleteMapping("/{userId}/{tripPostId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<Void> unsavePost(@PathVariable Long userId, @PathVariable Long tripPostId) {
        savedPostService.unsavePost(userId, tripPostId);
        return ResponseEntity.ok().build();
    }
}