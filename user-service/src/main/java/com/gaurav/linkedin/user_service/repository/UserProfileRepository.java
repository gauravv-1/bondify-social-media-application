package com.gaurav.linkedin.user_service.repository;

import com.gaurav.linkedin.user_service.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
