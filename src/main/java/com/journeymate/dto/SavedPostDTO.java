package com.journeymate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SavedPostDTO {
    private Long id;
    private Long userId;
    private Long tripPostId;
    private LocalDateTime savedAt;
}