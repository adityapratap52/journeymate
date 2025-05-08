package com.journeymate.repository.trip;

import com.journeymate.model.trip.SavedPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedPostRepository extends JpaRepository<SavedPost, Long> {
    List<SavedPost> findByUserId(Long userId);
    List<SavedPost> findByTripPostId(Long tripPostId);
    Optional<SavedPost> findByUserIdAndTripPostId(Long userId, Long tripPostId);
    boolean existsByUserIdAndTripPostId(Long userId, Long tripPostId);
    void deleteByUserIdAndTripPostId(Long userId, Long tripPostId);
}