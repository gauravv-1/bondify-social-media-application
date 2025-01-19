package com.gaurav.linkedin.user_service.repository;

import com.gaurav.linkedin.user_service.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("SELECT u.profilePicUrl FROM UserProfile u WHERE u.user.id = :userId")
    String findProfilePicUrlByUserId(@Param("userId") Long userId);
}
