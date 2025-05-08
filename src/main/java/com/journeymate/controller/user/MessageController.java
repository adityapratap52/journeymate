package com.journeymate.controller.user;

import com.journeymate.dto.MessageDTO;
import com.journeymate.service.MessageService;
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
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Message management APIs")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {
    
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(
        summary = "Send a message",
        description = "Creates a new message between users",
        responses = {
            @ApiResponse(responseCode = "200", description = "Message sent successfully",
                    content = @Content(schema = @Schema(implementation = MessageDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @PostMapping
    @PreAuthorize("isAuthenticated() and #messageDTO.senderId == authentication.principal.username")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(messageService.createMessage(messageDTO));
    }

    @Operation(
        summary = "Get all messages",
        description = "Retrieves all messages (Admin only)",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @Operation(
        summary = "Get message by ID",
        description = "Retrieves a specific message by its ID",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved message"),
            @ApiResponse(responseCode = "404", description = "Message not found"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @messageService.getMessageById(#id).senderId == authentication.principal.username " +
                  "or @messageService.getMessageById(#id).receiverId == authentication.principal.username")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @Operation(
        summary = "Get sent messages",
        description = "Retrieves all messages sent by a user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/sent/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<List<MessageDTO>> getSentMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getMessagesBySenderId(userId));
    }

    @Operation(
        summary = "Get received messages",
        description = "Retrieves all messages received by a user",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/received/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.username")
    public ResponseEntity<List<MessageDTO>> getReceivedMessages(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getMessagesByReceiverId(userId));
    }

    @Operation(
        summary = "Get messages between users",
        description = "Retrieves all messages between two users",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved messages"),
            @ApiResponse(responseCode = "403", description = "Access forbidden")
        }
    )
    @GetMapping("/between/{senderId}/{receiverId}")
    @PreAuthorize("hasRole('ADMIN') or #senderId == authentication.principal.username " +
                  "or #receiverId == authentication.principal.username")
    public ResponseEntity<List<MessageDTO>> getMessagesBetweenUsers(
            @PathVariable Long senderId, @PathVariable Long receiverId) {
        return ResponseEntity.ok(messageService.getMessagesBetweenUsers(senderId, receiverId));
    }

    @Operation(
        summary = "Delete message",
        description = "Deletes an existing message",
        responses = {
            @ApiResponse(responseCode = "200", description = "Message deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access forbidden"),
            @ApiResponse(responseCode = "404", description = "Message not found")
        }
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @messageService.getMessageById(#id).senderId == authentication.principal.username")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
}