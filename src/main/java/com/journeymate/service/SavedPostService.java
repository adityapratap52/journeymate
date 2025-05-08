package com.journeymate.service;

import com.journeymate.dto.SavedPostDTO;
import com.journeymate.model.trip.SavedPost;
import com.journeymate.model.trip.TripPost;
import com.journeymate.model.user.User;
import com.journeymate.repository.trip.SavedPostRepository;
import com.journeymate.repository.trip.TripPostRepository;
import com.journeymate.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SavedPostService {
    private final SavedPostRepository savedPostRepository;
    private final TripPostRepository tripPostRepository;
    private final UserRepository userRepository;

    public SavedPostService(SavedPostRepository savedPostRepository,
                          TripPostRepository tripPostRepository,
                          UserRepository userRepository) {
        this.savedPostRepository = savedPostRepository;
        this.tripPostRepository = tripPostRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SavedPostDTO savePost(SavedPostDTO savedPostDTO) {
        if (savedPostRepository.existsByUserIdAndTripPostId(
                savedPostDTO.getUserId(), savedPostDTO.getTripPostId())) {
            throw new RuntimeException("Post already saved by user");
        }

        User user = userRepository.findById(savedPostDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        TripPost tripPost = tripPostRepository.findById(savedPostDTO.getTripPostId())
                .orElseThrow(() -> new RuntimeException("Trip post not found"));

        SavedPost savedPost = new SavedPost();
        savedPost.setUser(user);
        savedPost.setTripPost(tripPost);
        
        SavedPost saved = savedPostRepository.save(savedPost);
        return convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<SavedPostDTO> getAllSavedPosts() {
        return savedPostRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SavedPostDTO> getSavedPostsByUserId(Long userId) {
        return savedPostRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SavedPostDTO> getSavedPostsByTripPostId(Long tripPostId) {
        return savedPostRepository.findByTripPostId(tripPostId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unsavePost(Long userId, Long tripPostId) {
        savedPostRepository.deleteByUserIdAndTripPostId(userId, tripPostId);
    }

    @Transactional(readOnly = true)
    public boolean isPostSavedByUser(Long userId, Long tripPostId) {
        return savedPostRepository.existsByUserIdAndTripPostId(userId, tripPostId);
    }

    private SavedPostDTO convertToDTO(SavedPost savedPost) {
        SavedPostDTO dto = new SavedPostDTO();
        dto.setId(savedPost.getId());
        dto.setUserId(savedPost.getUser().getId());
        dto.setTripPostId(savedPost.getTripPost().getId());
        dto.setSavedAt(savedPost.getSavedAt());
        return dto;
    }
}