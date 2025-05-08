package com.journeymate.service;

import com.journeymate.dto.TripPostDTO;
import com.journeymate.model.trip.TripImage;
import com.journeymate.model.trip.TripPost;
import com.journeymate.model.user.User;
import com.journeymate.repository.trip.TripPostRepository;
import com.journeymate.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {
    private final TripPostRepository tripPostRepository;
    private final UserRepository userRepository;

    public TripService(TripPostRepository tripPostRepository, UserRepository userRepository) {
        this.tripPostRepository = tripPostRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TripPostDTO createTripPost(TripPostDTO tripPostDTO) {
        User user = userRepository.findById(tripPostDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TripPost tripPost = new TripPost();
        updateTripPostFromDTO(tripPost, tripPostDTO, user);
        
        // Handle images
        if (tripPostDTO.getImageUrls() != null) {
            tripPostDTO.getImageUrls().forEach(imageUrl -> {
                TripImage tripImage = new TripImage();
                tripImage.setImageUrl(imageUrl);
                tripImage.setTripPost(tripPost);
                tripPost.getImages().add(tripImage);
            });
        }

        TripPost savedTripPost = tripPostRepository.save(tripPost);
        return convertToDTO(savedTripPost);
    }

    @Transactional(readOnly = true)
    public List<TripPostDTO> getAllTripPosts() {
        return tripPostRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripPostDTO getTripPostById(Long id) {
        TripPost tripPost = tripPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip post not found"));
        return convertToDTO(tripPost);
    }

    @Transactional(readOnly = true)
    public List<TripPostDTO> getTripPostsByUserId(Long userId) {
        return tripPostRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripPostDTO updateTripPost(Long id, TripPostDTO tripPostDTO) {
        TripPost tripPost = tripPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trip post not found"));
        
        User user = userRepository.findById(tripPostDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        updateTripPostFromDTO(tripPost, tripPostDTO, user);
        
        // Update images
        tripPost.getImages().clear();
        if (tripPostDTO.getImageUrls() != null) {
            tripPostDTO.getImageUrls().forEach(imageUrl -> {
                TripImage tripImage = new TripImage();
                tripImage.setImageUrl(imageUrl);
                tripImage.setTripPost(tripPost);
                tripPost.getImages().add(tripImage);
            });
        }

        TripPost updatedTripPost = tripPostRepository.save(tripPost);
        return convertToDTO(updatedTripPost);
    }

    @Transactional
    public void deleteTripPost(Long id) {
        tripPostRepository.deleteById(id);
    }

    private void updateTripPostFromDTO(TripPost tripPost, TripPostDTO dto, User user) {
        tripPost.setUser(user);
        tripPost.setTitle(dto.getTitle());
        tripPost.setDescription(dto.getDescription());
        tripPost.setDestination(dto.getDestination());
        tripPost.setAmount(dto.getAmount());
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
    }

    private TripPostDTO convertToDTO(TripPost tripPost) {
        TripPostDTO dto = new TripPostDTO();
        dto.setId(tripPost.getId());
        dto.setUserId(tripPost.getUser().getId());
        dto.setTitle(tripPost.getTitle());
        dto.setDescription(tripPost.getDescription());
        dto.setDestination(tripPost.getDestination());
        dto.setAmount(tripPost.getAmount());
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
        dto.setCreatedDate(tripPost.getCreatedDate());
        dto.setModifiedDate(tripPost.getModifiedDate());
        
        dto.setImageUrls(tripPost.getImages().stream()
                .map(TripImage::getImageUrl)
                .collect(Collectors.toList()));
        
        return dto;
    }
}