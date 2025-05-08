package com.journeymate.controller.trip;

import com.journeymate.dto.TripFeedbackDTO;
import com.journeymate.service.TripFeedbackService;
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
@RequestMapping("/api/trip-feedback")
@Tag(name = "Trip Feedback", description = "Trip feedback management APIs")
@SecurityRequirement(name = "bearerAuth")
public class TripFeedbackController {
    
    private final TripFeedbackService tripFeedbackService;

    public TripFeedbackController(TripFeedbackService tripFeedbackService) {
        this.tripFeedbackService = tripFeedbackService;
    }

    @Operation(
        summary = "Create trip feedback",
        description = "Creates a new feedback for a trip",
        responses = {
            @ApiResponse(responseCode = "200", description = "Feedback created successfully",
                    content = @Content(schema = @Schema(implementation = TripFeedbackDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @PostMapping
    @PreAuthorize("isAuthenticated() and #feedbackDTO.fromUserId == authentication.principal.username")
    public ResponseEntity<TripFeedbackDTO> createFeedback(@RequestBody TripFeedbackDTO feedbackDTO) {
        return ResponseEntity.ok(tripFeedbackService.createFeedback(feedbackDTO));
    }

    @Operation(
        summary = "Get all feedback",
        description = "Retrieves all trip feedback (Admin only)",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved feedback"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TripFeedbackDTO>> getAllFeedback() {
        return ResponseEntity.ok(tripFeedbackService.getAllFeedback());
    }

    @Operation(
        summary = "Get feedback by ID",
        description = "Retrieves a specific feedback by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved feedback"),
            @ApiResponse(responseCode = "404", description = "Feedback not found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @tripFeedbackService.getFeedbackById(#id).fromUserId == authentication.principal.username " +
                  "or @tripFeedbackService.getFeedbackById(#id).toUserId == authentication.principal.username")
    public ResponseEntity<TripFeedbackDTO> getFeedbackById(@PathVariable Long id) {
        return ResponseEntity.ok(tripFeedbackService.getFeedbackById(id));
    }

    @Operation(
        summary = "Get feedback by trip post ID",
        description = "Retrieves all feedback for a specific trip post",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved feedback"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/trip/{tripPostId}")
    public ResponseEntity<List<TripFeedbackDTO>> getFeedbackByTripPostId(@PathVariable Long tripPostId) {
        return ResponseEntity.ok(tripFeedbackService.getFeedbackByTripPostId(tripPostId));
    }

    @Operation(
        summary = "Get feedback given by user",
        description = "Retrieves all feedback given by a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved feedback"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/from/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<List<TripFeedbackDTO>> getFeedbackByFromUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(tripFeedbackService.getFeedbackByFromUserId(userId));
    }

    @Operation(
        summary = "Get feedback received by user",
        description = "Retrieves all feedback received by a specific user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved feedback"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/to/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<List<TripFeedbackDTO>> getFeedbackByToUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(tripFeedbackService.getFeedbackByToUserId(userId));
    }

    @Operation(
        summary = "Update feedback",
        description = "Updates an existing feedback",
        responses = {
            @ApiResponse(responseCode = "200", description = "Feedback updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
        }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @tripFeedbackService.getFeedbackById(#id).fromUserId == authentication.principal.username")
    public ResponseEntity<TripFeedbackDTO> updateFeedback(@PathVariable Long id, @RequestBody TripFeedbackDTO feedbackDTO) {
        return ResponseEntity.ok(tripFeedbackService.updateFeedback(id, feedbackDTO));
    }

    @Operation(
        summary = "Delete feedback",
        description = "Deletes an existing feedback",
        responses = {
            @ApiResponse(responseCode = "200", description = "Feedback deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Feedback not found")
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @tripFeedbackService.getFeedbackById(#id).fromUserId == authentication.principal.username")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        tripFeedbackService.deleteFeedback(id);
        return ResponseEntity.ok().build();
    }
}