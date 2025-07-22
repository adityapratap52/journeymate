package com.journeymate.controller.trip;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.journeymate.dto.TripPostDTO;
import com.journeymate.dto.TripRequestDTO;
import com.journeymate.repository.trip.TripPostRepository;
import com.journeymate.service.ContextService;
import com.journeymate.service.TripService;
import com.journeymate.utils.Message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/")
@Tag(name = "Trip Posts", description = "Trip post management APIs")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TripController {
    
    private final TripService tripService;
    
    private final TripPostRepository tripPostRepository;
    
    private final ContextService contextService;

    @Operation(summary = "Create a new trip post", description = "Creates a new trip post with images",
        responses = {@ApiResponse(responseCode = "200", description = "Trip post created successfully",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")})
    @PostMapping(value = "add-trip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createTripPost(@RequestPart TripRequestDTO object, 
    		@RequestPart(value = "images", required = false)List<MultipartFile> images) {
    	tripService.createTripPost(object, images);
		return new ResponseEntity<>(new Message("Trip Post created successfully"), HttpStatus.CREATED);
    }
    
    @Operation(summary = "Update trip post", description = "Updates an existing trip post",
        responses = {@ApiResponse(responseCode = "200", description = "Trip post updated successfully",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Trip post not found")})
    @PostMapping(value = "update-trip/{uid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("@tripPostRepository.existsByUidAndCreaterIdAndDeletedFalse(#uid, @contextService.getCurrentUserId()) || hasRole('ADMIN')")
    public ResponseEntity<?> updateTripPost(@PathVariable String uid, @RequestPart TripRequestDTO object,
    						@RequestPart(value = "images", required = false)List<MultipartFile> images) {
        tripService.updateTripPost(uid, object, images);
		return new ResponseEntity<>(new Message("Trip Post updated successfully"), HttpStatus.OK);
    }
    
    @Operation(summary = "Get all trip posts", description = "Retrieves a list of all trip posts",
        responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved trip posts",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class)))})
    @GetMapping("getAllTrips")
    public ResponseEntity<List<TripPostDTO>> getAllTripPosts() {
        return ResponseEntity.ok(tripService.getAllTripPosts());
    }
    
    @Operation(summary = "Get trip post by ID", description = "Retrieves a specific trip post by its ID",
        responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved trip post",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "404", description = "Trip post not found")})
    @GetMapping("trip/{uid}")
    public ResponseEntity<TripPostDTO> getTripPostById(@PathVariable String uid) {
        return ResponseEntity.ok(tripService.getTripPostByUid(uid));
    }
    
    @Operation(summary = "Get trip posts by user ID", description = "Retrieves all trip posts created by a specific user",
        responses = {@ApiResponse(responseCode = "200", description = "Successfully retrieved user's trip posts",
                    content = @Content(schema = @Schema(implementation = TripPostDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @GetMapping("/tripsByUser")
    public ResponseEntity<List<TripPostDTO>> getTripPostsByUser() {
        return ResponseEntity.ok(tripService.getTripPostsByUser());
    }
    
    @Operation(summary = "Delete trip post", description = "Deletes an existing trip post",
        responses = {@ApiResponse(responseCode = "200", description = "Trip post deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Trip post not found")})
    @DeleteMapping("delete-trip/{uid}")
    @PreAuthorize("@tripPostRepository.existsByUidAndCreaterIdAndDeletedFalse(#uid, @contextService.getCurrentUserId()) || hasRole('ADMIN')")
    public ResponseEntity<?> deleteTripPost(@PathVariable String uid) {
        tripService.deleteTripPost(uid);
        return new ResponseEntity<>(new Message("Trip Post deleted successfully"), HttpStatus.OK);
    }
}