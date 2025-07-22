package com.journeymate.repository.trip;

import org.springframework.data.jpa.repository.JpaRepository;

import com.journeymate.model.trip.TripImage;

public interface TripImageRepository extends JpaRepository<TripImage, Long> {

}
