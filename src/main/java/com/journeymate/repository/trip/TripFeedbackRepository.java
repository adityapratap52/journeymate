package com.journeymate.repository.trip;

import com.journeymate.model.trip.TripFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripFeedbackRepository extends JpaRepository<TripFeedback, Long> {
    List<TripFeedback> findByTripPostId(Long tripPostId);
    List<TripFeedback> findByCreatedById(Long fromUserId);
    List<TripFeedback> findByModifiedById(Long toUserId);
}