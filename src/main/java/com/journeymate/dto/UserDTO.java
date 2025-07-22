package com.journeymate.dto;

import java.time.LocalDateTime;
import java.util.HashSet;

import com.journeymate.model.user.User;

import lombok.Data;

@Data
public class UserDTO {
    private String uid;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String mobileNo;
    private String occupation;
    private String address;
    private Integer age;
    private String gender;
    private String role;
    private String profileImage;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    
    public static User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setFullName(userDTO.getFullName());
        user.setMobileNo(userDTO.getMobileNo());
        user.setOccupation(userDTO.getOccupation());
        user.setAddress(userDTO.getAddress());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setRoles(new HashSet<>());
        return user;
    }

    public static UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUid(user.getUid());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setMobileNo(user.getMobileNo());
        dto.setOccupation(user.getOccupation());
        dto.setAddress(user.getAddress());
        dto.setAge(user.getAge());
        dto.setGender(user.getGender());
        dto.setCreatedDate(user.getCreatedDate());
        dto.setModifiedDate(user.getModifiedDate());
        dto.setRole(user.getRoles().stream()
                .map(role -> role.getRole())
                .findFirst().get());
        return dto;
    }
}