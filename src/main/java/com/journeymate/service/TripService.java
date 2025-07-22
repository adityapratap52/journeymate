package com.journeymate.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.journeymate.dto.TripPostDTO;
import com.journeymate.dto.TripRequestDTO;
import com.journeymate.model.trip.TripImage;
import com.journeymate.model.trip.TripPost;
import com.journeymate.model.user.User;
import com.journeymate.repository.trip.TripFeedbackRepository;
import com.journeymate.repository.trip.TripImageRepository;
import com.journeymate.repository.trip.TripPostRepository;
import com.journeymate.utils.CommonMethods;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TripService {

	private final TripPostRepository tripPostRepository;
	private final TripFeedbackRepository tripFeedbackRepository;
	private final TripImageRepository tripImageRepository;
	private final ContextService contextService;

	@Value("${upload.dir}")
	private String uploadDir;

	@Transactional
	public void createTripPost(TripRequestDTO tripPostDTO, List<MultipartFile> images) {

		TripPost tripPost = new TripPost();
		TripRequestDTO.requestMapper(tripPost, tripPostDTO);
		tripPost.setCreater(this.contextService.getCurrentUser());

		// Handle images
		if (images != null && !images.isEmpty()) {
			for (MultipartFile file : images) {
				if (!file.isEmpty()) {
					try {
						// ✅ Save file locally
						String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						Path filePath = Paths.get(uploadDir, fileName);
						Files.createDirectories(filePath.getParent()); // ensure folder exists
						Files.write(filePath, file.getBytes());

						// ✅ Save file reference in DB
						TripImage tripImage = new TripImage();
						tripImage.setImageUrl(uploadDir + fileName);
						tripImage.setTripPost(tripPost);
						tripPost.getImages().add(tripImage);
					} catch (IOException e) {
						throw new RuntimeException("Failed to save image file", e);
					}
				}
			}
		}

		tripPostRepository.save(tripPost);
	}

	@Transactional
	public void updateTripPost(String uid, TripRequestDTO tripPostDTO, List<MultipartFile> images) {
		TripPost tripPost = tripPostRepository.findByUidAndDeletedFalse(uid)
				.orElseThrow(() -> new RuntimeException("Trip post not found"));

		TripRequestDTO.requestMapper(tripPost, tripPostDTO);

		// delete previous image
		if (!tripPost.getImages().isEmpty()) {
			List<String> imageUrls = tripPost.getImages().stream().map(TripImage::getImageUrl).toList();
			for (String imageUrl : imageUrls) {
				// Extract local file path from the URL if needed
				// Example: if the URL is like "/uploads/images/image1.jpg", map it to actual
				// path
				File imageFile = new File(imageUrl); // adjust path here

				if (imageFile.exists()) {
					boolean deleted = imageFile.delete();
					if (deleted) {
						System.out.println("Deleted: " + imageFile.getPath());
					} else {
						System.err.println("Failed to delete: " + imageFile.getPath());
					}
				} else {
					System.out.println("File does not exist: " + imageFile.getPath());
				}
			}
			this.tripImageRepository.deleteAll(tripPost.getImages());
			tripPost.getImages().clear();
		}
		// Handle images
		if (images != null && !images.isEmpty()) {
			for (MultipartFile file : images) {
				if (!file.isEmpty()) {
					try {
						// ✅ Save file locally
						String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
						Path filePath = Paths.get(uploadDir, fileName);
						Files.createDirectories(filePath.getParent()); // ensure folder exists
						Files.write(filePath, file.getBytes());

						// ✅ Save file reference in DB
						TripImage tripImage = new TripImage();
						tripImage.setImageUrl(uploadDir + fileName); // web-accessible path
						tripImage.setTripPost(tripPost);
						tripPost.getImages().add(tripImage);
					} catch (IOException e) {
						throw new RuntimeException("Failed to save image file", e);
					}
				}
			}
		}

		tripPostRepository.save(tripPost);
	}

	@Transactional(readOnly = true)
	public List<TripPostDTO> getAllTripPosts() {
		List<TripPostDTO> data = tripPostRepository.findAllByDeletedFalse();
		if (!data.isEmpty()) {
			data.forEach(t -> {
				if (t.getImage() != null) {
					String encodeImage = CommonMethods.encodeImageToBase64(t.getImage());
					t.setImage("data:image/jpeg;base64," + encodeImage);
				}
				if (t.getTotalRating() != null) {
					t.setRating((double) Math.round((t.getTotalRating() / t.getTotalUser()) * 1.0));
				} else {
					t.setRating(0.0);
				}
			});
		}
		return data;
	}

	@Transactional(readOnly = true)
	public TripPostDTO getTripPostByUid(String uid) {
		TripPost tripPost = tripPostRepository.findByUidAndDeletedFalse(uid)
				.orElseThrow(() -> new RuntimeException("Trip post not found"));
		return TripPostDTO.convertToDTO(tripPost);
	}

	@Transactional(readOnly = true)
	public List<TripPostDTO> getTripPostsByUser() {
		User user = this.contextService.getCurrentUser();
		boolean isAdmin = user.getRoles().iterator().next().getRole().equalsIgnoreCase("ADMIN");
		List<TripPostDTO> data = tripPostRepository
				.findAllByDeletedFalseAndUserId(user.getId(), isAdmin);
		if (!data.isEmpty()) {
			data.forEach(t -> {
				if (t.getImage() != null) {
					String encodeImage = CommonMethods.encodeImageToBase64(t.getImage());
					t.setImage("data:image/jpeg;base64," + encodeImage);
				}
			});
		}
		return data;
	}

	@Transactional
	public void deleteTripPost(String uid) {
		TripPost post = tripPostRepository.findByUidAndDeletedFalse(uid)
				.orElseThrow(() -> new RuntimeException("Trip post not found"));
		if (!post.getTripFeedback().isEmpty())
			this.tripFeedbackRepository.deleteAll(post.getTripFeedback());
		if (!post.getImages().isEmpty()) {
			List<String> imageUrls = post.getImages().stream().map(TripImage::getImageUrl).toList();
			if (!imageUrls.isEmpty()) {
				for (String imageUrl : imageUrls) {
					// Extract local file path from the URL if needed
					// Example: if the URL is like "/uploads/images/image1.jpg", map it to actual
					// path
					File imageFile = new File(imageUrl); // adjust path here

					if (imageFile.exists()) {
						boolean deleted = imageFile.delete();
						if (deleted) {
							System.out.println("Deleted: " + imageFile.getPath());
						} else {
							System.err.println("Failed to delete: " + imageFile.getPath());
						}
					} else {
						System.out.println("File does not exist: " + imageFile.getPath());
					}
				}
			}
			this.tripImageRepository.deleteAll(post.getImages());
		}
		tripPostRepository.deleteByUid(uid);
	}
}