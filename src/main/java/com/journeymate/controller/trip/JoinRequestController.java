package com.journeymate.controller.trip;

import com.journeymate.dto.JoinRequestDTO;
import com.journeymate.service.JoinRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/")
@Tag(name = "Join Requests", description = "Join request management APIs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class JoinRequestController {
    
    private final JoinRequestService joinRequestService;

    @Operation(summary = "Create a join request", 
        description = "Creates a new join request for a trip post",
        responses = {@ApiResponse(responseCode = "200", description = "Join request created successfully",
                    content = @Content(schema = @Schema(implementation = JoinRequestDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @PostMapping("send-request")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<JoinRequestDTO> createJoinRequest(@RequestBody JoinRequestDTO joinRequestDTO) {
        return ResponseEntity.ok(joinRequestService.createJoinRequest(joinRequestDTO));
    }

    @Operation(summary = "Get all join requests",
        description = "Retrieves all join requests (Admin only)",
        responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved join requests"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<JoinRequestDTO>> getAllJoinRequests() {
        return ResponseEntity.ok(joinRequestService.getAllJoinRequests());
    }

    @Operation(summary = "Get join request by ID",
        description = "Retrieves a specific join request by its ID",
        responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved join request"),
            @ApiResponse(responseCode = "404", description = "Join request not found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @joinRequestService.getJoinRequestById(#id).userId == authentication.principal.username")
    public ResponseEntity<JoinRequestDTO> getJoinRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(joinRequestService.getJoinRequestById(id));
    }

    @Operation(summary = "Get join requests by trip post ID",
        description = "Retrieves all join requests for a specific trip post",
        responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved join requests"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @GetMapping("/trip/{tripPostId}")
    @PreAuthorize("hasRole('ADMIN') or @tripService.getTripPostById(#tripPostId).userId == authentication.principal.username")
    public ResponseEntity<List<JoinRequestDTO>> getJoinRequestsByTripPostId(@PathVariable Long tripPostId) {
        return ResponseEntity.ok(joinRequestService.getJoinRequestsByTripPostId(tripPostId));
    }

    @Operation(summary = "Get join requests by user ID",
        description = "Retrieves all join requests made by a specific user",
        responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved join requests"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @GetMapping("/requestByUser/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<List<JoinRequestDTO>> getJoinRequestsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(joinRequestService.getJoinRequestsByUserId(userId));
    }

    @Operation(summary = "Update join request",
        description = "Updates an existing join request",
        responses = {@ApiResponse(responseCode = "200", description = "Join request updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Join request not found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @PutMapping("update-request/{uid}")
    @PreAuthorize("hasRole('ADMIN') or @joinRequestService.findByUidAndDeletedFalse(#uid).get().userId == authentication.principal.username")
    public ResponseEntity<JoinRequestDTO> updateJoinRequest(@PathVariable String uid, @RequestBody JoinRequestDTO joinRequestDTO) {
        return ResponseEntity.ok(joinRequestService.updateJoinRequest(uid, joinRequestDTO));
    }

    @Operation(summary = "Delete join request",
        description = "Deletes an existing join request",
        responses = {@ApiResponse(responseCode = "200", description = "Join request deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Join request not found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @DeleteMapping("deleteRequest/{id}")
    @PreAuthorize("hasRole('ADMIN') or @joinRequestService.getJoinRequestById(#id).userId == authentication.principal.username")
    public ResponseEntity<Void> deleteJoinRequest(@PathVariable Long id) {
        joinRequestService.deleteJoinRequest(id);
        return ResponseEntity.ok().build();
    }
}