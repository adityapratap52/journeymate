package com.journeymate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JoinRequestDTO {
    private Long id;
    private Long tripPostId;
    private Long userId;
    private String requesterName;
    private String contactInfo;
    private String occupation;
    private String address;
    private Integer age;
    private String gender;
    private Integer personQuantity;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}