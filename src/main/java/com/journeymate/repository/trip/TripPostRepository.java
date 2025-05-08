package com.journeymate.repository.trip;

import com.journeymate.model.trip.TripPost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripPostRepository extends JpaRepository<TripPost, Long> {
    List<TripPost> findByUserId(Long userId);
}