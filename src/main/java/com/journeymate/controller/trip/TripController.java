package com.journeymate.controller.trip;

import com.journeymate.dto.TripPostDTO;
import com.journeymate.service.TripService;
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
@RequestMapping("/api/trips")
@Tag(name = "Trip Posts", description = "Trip post management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TripController {
    
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @Operation(
        summary = "Create a new trip post",
        description = "Creates a new trip post with images",
        responses = {
            @ApiResponse(responseCode = "200", description = "Trip post created successfully",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TripPostDTO> createTripPost(@RequestBody TripPostDTO tripPostDTO) {
        TripPostDTO created = tripService.createTripPost(tripPostDTO);
        return ResponseEntity.ok(created);
    }

    @Operation(
        summary = "Get all trip posts",
        description = "Retrieves a list of all trip posts",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trip posts",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class)))
        }
    )
    @GetMapping
    public ResponseEntity<List<TripPostDTO>> getAllTripPosts() {
        return ResponseEntity.ok(tripService.getAllTripPosts());
    }

    @Operation(
        summary = "Get trip post by ID",
        description = "Retrieves a specific trip post by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trip post",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "404", description = "Trip post not found")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<TripPostDTO> getTripPostById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripPostById(id));
    }

    @Operation(
        summary = "Get trip posts by user ID",
        description = "Retrieves all trip posts created by a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's trip posts",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
        }
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TripPostDTO>> getTripPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(tripService.getTripPostsByUserId(userId));
    }

    @Operation(
        summary = "Update trip post",
        description = "Updates an existing trip post",
        responses = {
            @ApiResponse(responseCode = "200", description = "Trip post updated successfully",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Trip post not found")
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("@tripService.getTripPostById(#id).userId == authentication.principal.username || hasRole('ADMIN')")
    public ResponseEntity<TripPostDTO> updateTripPost(@PathVariable Long id, @RequestBody TripPostDTO tripPostDTO) {
        return ResponseEntity.ok(tripService.updateTripPost(id, tripPostDTO));
    }

    @Operation(
        summary = "Delete trip post",
        description = "Deletes an existing trip post",
        responses = {
            @ApiResponse(responseCode = "200", description = "Trip post deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Trip post not found")
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("@tripService.getTripPostById(#id).userId == authentication.principal.username || hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTripPost(@PathVariable Long id) {
        tripService.deleteTripPost(id);
        return ResponseEntity.ok().build();
    }
}