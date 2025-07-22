package com.journeymate.repository.user;

import com.journeymate.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    @Query("SELECT id FROM User WHERE not deleted AND username=:username")
	Optional<Long> findUserIdByUsername(String username);
    
	User findByUidAndEnabledTrueAndDeletedFalse(String uid);
}