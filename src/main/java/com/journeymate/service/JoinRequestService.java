package com.journeymate.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journeymate.dto.JoinRequestDTO;
import com.journeymate.model.trip.JoinRequest;
import com.journeymate.model.trip.TripPost;
import com.journeymate.model.user.User;
import com.journeymate.repository.trip.JoinRequestRepository;
import com.journeymate.repository.trip.TripPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JoinRequestService {
    private final JoinRequestRepository joinRequestRepository;
    private final TripPostRepository tripPostRepository;
    private final ContextService contextService;

    @Transactional
    public JoinRequestDTO createJoinRequest(JoinRequestDTO joinRequestDTO) {
        User user = this.contextService.getCurrentUser();
        
        TripPost tripPost = tripPostRepository.findByUidAndDeletedFalse(joinRequestDTO.getTripPostUid())
                .orElseThrow(() -> new RuntimeException("Trip post not found"));

        JoinRequest joinRequest = new JoinRequest();
        updateJoinRequestFromDTO(joinRequest, joinRequestDTO, user, tripPost);
        
        JoinRequest savedJoinRequest = joinRequestRepository.save(joinRequest);
        return convertToDTO(savedJoinRequest);
    }

    @Transactional(readOnly = true)
    public List<JoinRequestDTO> getAllJoinRequests() {
        return joinRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JoinRequestDTO getJoinRequestById(Long id) {
        JoinRequest joinRequest = joinRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Join request not found"));
        return convertToDTO(joinRequest);
    }

    @Transactional(readOnly = true)
    public List<JoinRequestDTO> getJoinRequestsByTripPostId(Long tripPostId) {
        return joinRequestRepository.findByTripPostId(tripPostId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JoinRequestDTO> getJoinRequestsByUserId(Long userId) {
        return joinRequestRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public JoinRequestDTO updateJoinRequest(String uid, JoinRequestDTO joinRequestDTO) {
        JoinRequest joinRequest = joinRequestRepository.findByUidAndDeletedFalse(uid)
                .orElseThrow(() -> new RuntimeException("Join request not found"));
        
        User user = this.contextService.getCurrentUser();
        
        TripPost tripPost = tripPostRepository.findByUidAndDeletedFalse(joinRequestDTO.getTripPostUid())
                .orElseThrow(() -> new RuntimeException("Trip post not found"));

        updateJoinRequestFromDTO(joinRequest, joinRequestDTO, user, tripPost);
        
        JoinRequest updatedJoinRequest = joinRequestRepository.save(joinRequest);
        return convertToDTO(updatedJoinRequest);
    }

    @Transactional
    public void deleteJoinRequest(Long id) {
        joinRequestRepository.deleteById(id);
    }

    private void updateJoinRequestFromDTO(JoinRequest joinRequest, JoinRequestDTO dto, User user, TripPost tripPost) {
        joinRequest.setUser(user);
        joinRequest.setTripPost(tripPost);
        joinRequest.setAge(dto.getAge());
        joinRequest.setGender(dto.getGender());
        joinRequest.setPersonQuantity(dto.getPersonQuantity());
        joinRequest.setStatus(dto.getStatus());
    }

    private JoinRequestDTO convertToDTO(JoinRequest joinRequest) {
        JoinRequestDTO dto = new JoinRequestDTO();
        dto.setId(joinRequest.getId());
        dto.setAge(joinRequest.getAge());
        dto.setGender(joinRequest.getGender());
        dto.setPersonQuantity(joinRequest.getPersonQuantity());
        dto.setStatus(joinRequest.getStatus());
        return dto;
    }
}