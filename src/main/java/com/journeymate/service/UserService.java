package com.journeymate.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.journeymate.dto.PasswordResetRequest;
import com.journeymate.dto.UserDTO;
import com.journeymate.exception.CustomException;
import com.journeymate.exception.DuplicateResourceException;
import com.journeymate.model.user.Role;
import com.journeymate.model.user.User;
import com.journeymate.repository.user.RoleRepository;
import com.journeymate.repository.user.UserRepository;
import com.journeymate.utils.CommonMethods;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContextService contextService;
    
	@Value("${upload.dir}")
	private String uploadDir;

    public Map<String, String> createUser(UserDTO userDTO) {
        // Validate unique constraints first
        
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new DuplicateResourceException("Email is already in use");
        }
        
        Map<String, String> data = new HashMap<>();
        User user = UserDTO.convertToEntity(userDTO);

    	String username = userDTO.getFullName().split(" ")[0];
    	username += LocalDateTime.now().toString().substring(20); 
    	user.setUsername(username);
    	
        // Encode password
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        Role userRole = roleRepository.findByRole("USER")
                .orElseThrow(() -> new CustomException("Default role not found"));
        
        user.getRoles().add(userRole);
        this.userRepository.save(user);
        data.put("username", username);
        return data;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO getUserByUid(String uid) {
        User user = userRepository.findByUidAndEnabledTrueAndDeletedFalse(uid);
        if(user == null) throw new CustomException("User not found or disabled!!!");
        UserDTO userDto = UserDTO.convertToDTO(user);
        if (user.getProfileImage() != null) {
			String encodeImage = CommonMethods.encodeImageToBase64(uploadDir+"profiles/"+user.getProfileImage());
			userDto.setProfileImage("data:image/jpeg;base64," + encodeImage);
		}
        return userDto;
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDetails.getUsername() != null && !userDetails.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new RuntimeException("Username is already taken");
            }
            user.setUsername(userDetails.getUsername());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(userDetails.getEmail())) {
                throw new RuntimeException("Email is already in use");
            }
            user.setEmail(userDetails.getEmail());
        }

        user.setFullName(userDetails.getFullName());
        user.setMobileNo(userDetails.getMobileNo());
        user.setOccupation(userDetails.getOccupation());
        user.setAddress(userDetails.getAddress());
        user.setAge(userDetails.getAge());
        user.setGender(userDetails.getGender());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void resetPassword(PasswordResetRequest request) {
        User user = userRepository.findByUsername(this.contextService.getCurrentUsername())
                .orElseThrow(() -> new CustomException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new CustomException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

	public Map<String, String> updateProfileImage(MultipartFile image) {
		User user = this.contextService.getCurrentUser();
		String fileName = null;
		
		// add profile image in directory
		if (image != null && !image.isEmpty()) {
			try {
				// ✅ Save file locally
				String imageName = image.getOriginalFilename();
				fileName = UUID.randomUUID() + imageName.substring(imageName.lastIndexOf("."), imageName.length());
				Path filePath = Paths.get(uploadDir+"profiles/", fileName);
				Files.createDirectories(filePath.getParent()); // ensure folder exists
				Files.write(filePath, image.getBytes());
			} catch (IOException e) {
				throw new RuntimeException("Failed to save image file", e);
			}
		}
		
		// delete existing profile image
		if(user.getProfileImage() != null) {
			File imageFile = new File(uploadDir+"profiles/", user.getProfileImage()); // adjust path here

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
		user.setProfileImage(fileName);
		
		Map<String, String> data = new HashMap<>();
		if(user.getProfileImage() != null) {
			data.put("image", "data:image/jpeg;base64," + CommonMethods.encodeImageToBase64(uploadDir+"profiles/"+user.getProfileImage()));
		}
		
		return data;
	}
}