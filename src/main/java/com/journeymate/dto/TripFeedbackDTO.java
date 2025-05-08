package com.journeymate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TripFeedbackDTO {
    private Long id;
    private Long tripPostId;
    private Long fromUserId;
    private Long toUserId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}