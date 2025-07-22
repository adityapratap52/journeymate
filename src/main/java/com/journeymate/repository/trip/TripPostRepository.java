package com.journeymate.repository.trip;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.journeymate.dto.DBResponse;
import com.journeymate.dto.TripPostDTO;
import com.journeymate.model.trip.TripPost;

public interface TripPostRepository extends JpaRepository<TripPost, Long> {

    @Query("SELECT new com.journeymate.dto.TripPostDTO(tp.id, tp.uid, tp.title, tp.description, tp.destination, tp.price, "
    		+ "tp.gender, tp.personType, tp.tripStartingDate, tp.tripEndingDate, tp.postExpireDate, tp.tripDuration, tp.creater.fullName, min(i.imageUrl), SUM(tf.rating), COUNT(tf.id)) "
    		+ "FROM TripPost tp LEFT JOIN tp.tripFeedback tf "
    		+ "LEFT JOIN tp.images i ON not i.deleted WHERE not tp.deleted AND tp.postExpireDate >= current_date() GROUP BY tp.id")
	List<TripPostDTO> findAllByDeletedFalse();
    
    @Query("SELECT new com.journeymate.dto.TripPostDTO(tp.id, tp.uid, tp.creater.uid, tp.creater.fullName, tp.title, tp.description, tp.destination, tp.price, "
    		+ "tp.gender, tp.personType, tp.tripStartingDate, tp.tripEndingDate, tp.postExpireDate, tp.tripDuration, min(i.imageUrl), SUM(tf.rating), COUNT(tf.id)) "
    		+ "FROM TripPost tp LEFT JOIN tp.tripFeedback tf "
    		+ "LEFT JOIN tp.images i ON not i.deleted WHERE not tp.deleted AND (:isAdmin = TRUE OR tp.creater.id=:userId) GROUP BY tp.id")
	List<TripPostDTO> findAllByDeletedFalseAndUserId(Long userId, boolean isAdmin);
    
    @Query(value="SELECT trip_post_id AS first, min(image_url) AS second FROM trip_images WHERE not deleted AND trip_post_id IN (:postIds) GROUP BY trip_post_id", nativeQuery=true)
    List<DBResponse> getImagesByPostIds(List<Long> postIds);

	Optional<TripPost> findByUidAndDeletedFalse(String uid);
	
	boolean existsByUidAndCreaterIdAndDeletedFalse(String uid, Long userId);

	void deleteByUid(String uid);
}