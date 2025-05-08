package com.journeymate.repository.trip;

import com.journeymate.model.trip.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
    List<JoinRequest> findByTripPostId(Long tripPostId);
    List<JoinRequest> findByUserId(Long userId);
    List<JoinRequest> findByTripPostIdAndStatus(Long tripPostId, String status);
}