package com.journeymate.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.journeymate.model.trip.TripFeedback;
import com.journeymate.model.trip.TripImage;
import com.journeymate.model.trip.TripPost;
import com.journeymate.utils.CommonMethods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"id", "totalRating", "totalUser"})
public class TripPostDTO {
	
	private Long id;
    private String uid;
    private String createrUid;
    private String createrName;
    private String title;
    private String description;
    private String destination;
    private BigDecimal amount;
    private long duration;
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
    private List<String> images;
    private String image; 
    private Double rating;
    private Long totalRating;
    private Long totalUser;
    
    public TripPostDTO(Long id, String uid, String title, String description, String destination, BigDecimal amount,
			String gender, String personType, LocalDate tripStartingDate, LocalDate tripEndingDate, LocalDate postExpireDate, long duration, 
			String createrName, String image, Long totalRating, Long totalUser) {
    	this.id = id;
		this.uid = uid;
		this.title = title;
		this.description = description;
		this.destination = destination;
		this.amount = amount;
		this.gender = gender;
		this.personType = personType;
		this.tripStartingDate = tripStartingDate;
		this.tripEndingDate = tripEndingDate;
		this.postExpireDate = postExpireDate;
		this.duration = duration;
		this.createrName = createrName;
		this.image = image;
		this.totalRating = totalRating;
		this.totalUser = totalUser;
	}
    
    public TripPostDTO(Long id, String uid, String createrUid, String createrName, String title, String description, String destination, BigDecimal amount,
			String gender, String personType, LocalDate tripStartingDate, LocalDate tripEndingDate, LocalDate postExpireDate, long duration, 
			String image, Long totalRating, Long totalUser) {
    	this.id = id;
		this.uid = uid;
		this.createrUid = createrUid;
		this.createrName = createrName;
		this.title = title;
		this.description = description;
		this.destination = destination;
		this.amount = amount;
		this.gender = gender;
		this.personType = personType;
		this.tripStartingDate = tripStartingDate;
		this.tripEndingDate = tripEndingDate;
		this.postExpireDate = postExpireDate;
		this.duration = duration;
		this.image = image;
		this.totalRating = totalRating;
		this.totalUser = totalUser;
	}
    
    public static TripPostDTO convertToDTO(TripPost tripPost) {
        TripPostDTO dto = new TripPostDTO();
        dto.setUid(tripPost.getUid());
        dto.setCreaterUid(tripPost.getCreater().getUid());
        dto.setCreaterName(tripPost.getCreater().getFullName());
        dto.setTitle(tripPost.getTitle());
        dto.setDescription(tripPost.getDescription());
        dto.setDestination(tripPost.getDestination());
        dto.setAmount(tripPost.getPrice());
        dto.setPreference(tripPost.getPreference());
        dto.setMinAge(tripPost.getMinAge());
        dto.setMaxAge(tripPost.getMaxAge());
        dto.setGender(tripPost.getGender());
        dto.setPersonType(tripPost.getPersonType());
        dto.setPersonCount(tripPost.getPersonCount());
        dto.setPostExpireDate(tripPost.getPostExpireDate());
        dto.setTripStartingDate(tripPost.getTripStartingDate());
        dto.setTripEndingDate(tripPost.getTripEndingDate());
        dto.setTripTransportation(tripPost.getTripTransportation());
        
        List<TripFeedback> tf = tripPost.getTripFeedback();
        Double totalRating = tf.stream().collect(Collectors.summarizingDouble(t->t.getRating())).getSum();
        
        if(totalRating != null && totalRating > 0) {
        	dto.setRating(totalRating/tf.size());
        }
        
    	List<String> images = new ArrayList<>();
        try {
        	for(TripImage image : tripPost.getImages()) {            	
        		String base64Image = CommonMethods.encodeImageToBase64(image.getImageUrl());
        	    if (base64Image != null) {
        	        // Add Data URI prefix so frontend can directly show the image
        	    	images.add("data:image/jpeg;base64," + base64Image);
        	    }
            	
            }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        dto.setImages(images);        
        return dto;
    }
}