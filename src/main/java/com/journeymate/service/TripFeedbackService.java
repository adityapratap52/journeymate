package com.journeymate.service;

import com.journeymate.dto.TripFeedbackDTO;
import com.journeymate.model.trip.TripFeedback;
import com.journeymate.model.trip.TripPost;
import com.journeymate.model.user.User;
import com.journeymate.repository.trip.TripFeedbackRepository;
import com.journeymate.repository.trip.TripPostRepository;
import com.journeymate.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripFeedbackService {
    private final TripFeedbackRepository tripFeedbackRepository;
    private final TripPostRepository tripPostRepository;
    private final UserRepository userRepository;

    public TripFeedbackService(TripFeedbackRepository tripFeedbackRepository,
                             TripPostRepository tripPostRepository,
                             UserRepository userRepository) {
        this.tripFeedbackRepository = tripFeedbackRepository;
        this.tripPostRepository = tripPostRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TripFeedbackDTO createFeedback(TripFeedbackDTO feedbackDTO) {
        TripPost tripPost = tripPostRepository.findById(feedbackDTO.getTripPostId())
                .orElseThrow(() -> new RuntimeException("Trip post not found"));
        
        User fromUser = userRepository.findById(feedbackDTO.getFromUserId())
                .orElseThrow(() -> new RuntimeException("From user not found"));
        
        User toUser = userRepository.findById(feedbackDTO.getToUserId())
                .orElseThrow(() -> new RuntimeException("To user not found"));

        if (feedbackDTO.getRating() < 1 || feedbackDTO.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        TripFeedback feedback = new TripFeedback();
        feedback.setTripPost(tripPost);
        feedback.setCreatedBy(fromUser);
        feedback.setModifiedBy(toUser);
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComment(feedbackDTO.getComment());
        
        TripFeedback savedFeedback = tripFeedbackRepository.save(feedback);
        return convertToDTO(savedFeedback);
    }

    @Transactional(readOnly = true)
    public List<TripFeedbackDTO> getAllFeedback() {
        return tripFeedbackRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripFeedbackDTO getFeedbackById(Long id) {
        TripFeedback feedback = tripFeedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return convertToDTO(feedback);
    }

    @Transactional(readOnly = true)
    public List<TripFeedbackDTO> getFeedbackByTripPostId(Long tripPostId) {
        return tripFeedbackRepository.findByTripPostId(tripPostId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TripFeedbackDTO> getFeedbackByFromUserId(Long fromUserId) {
        return tripFeedbackRepository.findByCreatedById(fromUserId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TripFeedbackDTO> getFeedbackByToUserId(Long toUserId) {
        return tripFeedbackRepository.findByModifiedById(toUserId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripFeedbackDTO updateFeedback(Long id, TripFeedbackDTO feedbackDTO) {
        TripFeedback feedback = tripFeedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (feedbackDTO.getRating() != null) {
            if (feedbackDTO.getRating() < 1 || feedbackDTO.getRating() > 5) {
                throw new RuntimeException("Rating must be between 1 and 5");
            }
            feedback.setRating(feedbackDTO.getRating());
        }
        
        if (feedbackDTO.getComment() != null) {
            feedback.setComment(feedbackDTO.getComment());
        }

        TripFeedback updatedFeedback = tripFeedbackRepository.save(feedback);
        return convertToDTO(updatedFeedback);
    }

    @Transactional
    public void deleteFeedback(Long id) {
        tripFeedbackRepository.deleteById(id);
    }

    private TripFeedbackDTO convertToDTO(TripFeedback feedback) {
        TripFeedbackDTO dto = new TripFeedbackDTO();
        dto.setId(feedback.getId());
        dto.setTripPostId(feedback.getTripPost().getId());
        dto.setFromUserId(feedback.getCreatedBy().getId());
        dto.setToUserId(feedback.getModifiedBy().getId());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedDate(feedback.getCreatedDate());
        dto.setModifiedDate(feedback.getModifiedDate());
        return dto;
    }
}