package com.journeymate.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data 
public class JoinRequestDTO {
    private Long id;
    private String tripPostUid;
    private Integer age;
    private String gender;
    private Integer personQuantity;
    private String status;
    private String message;
}