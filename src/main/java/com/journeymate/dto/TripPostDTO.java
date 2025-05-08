package com.journeymate.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class TripPostDTO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private String destination;
    private BigDecimal amount;
    private String preference;
    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private String personType;
    private Integer personCount;
    private LocalDateTime postExpireDate;
    private LocalDate tripStartingDate;
    private LocalDate tripEndingDate;
    private String tripTransportation;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<String> imageUrls = new ArrayList<>();
}