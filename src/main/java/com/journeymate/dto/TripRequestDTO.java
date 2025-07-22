package com.journeymate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.journeymate.model.trip.TripPost;

import lombok.Data;

@Data
public class TripRequestDTO {
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
    private LocalDate postExpireDate;
    private LocalDate tripStartingDate;
    private LocalDate tripEndingDate;
    private String tripTransportation;    
    
	public static void requestMapper(TripPost tripPost, TripRequestDTO dto) {
	    tripPost.setTitle(dto.getTitle());
	    tripPost.setDescription(dto.getDescription());
	    tripPost.setDestination(dto.getDestination());
	    tripPost.setPrice(dto.getAmount());
	    tripPost.setPreference(dto.getPreference());
	    tripPost.setMinAge(dto.getMinAge());
	    tripPost.setMaxAge(dto.getMaxAge());
	    tripPost.setGender(dto.getGender());
	    tripPost.setPersonType(dto.getPersonType());
	    tripPost.setPersonCount(dto.getPersonCount());
	    tripPost.setPostExpireDate(dto.getPostExpireDate());
	    tripPost.setTripStartingDate(dto.getTripStartingDate());
	    tripPost.setTripEndingDate(dto.getTripEndingDate());
	    tripPost.setTripTransportation(dto.getTripTransportation());
	    tripPost.setTripDuration(ChronoUnit.DAYS.between(dto.getTripStartingDate(), dto.getTripEndingDate()));
	}
}
