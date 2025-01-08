package com.gaurav.linkedin.user_service.repository;

import com.gaurav.linkedin.user_service.dto.UserSearchResultDto;
import com.gaurav.linkedin.user_service.entity.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT new com.gaurav.linkedin.user_service.dto.UserSearchResultDto(u.id, u.name, up.username, up.profilePicUrl) " +
            "FROM User u LEFT JOIN u.userProfile up " +
            "WHERE (LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR (up.username IS NOT NULL AND LOWER(up.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')))) " +
            "ORDER BY CASE WHEN LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 0 ELSE 1 END, " +
            "CASE WHEN LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN LOCATE(LOWER(:searchTerm), LOWER(u.name)) ELSE -1 END ASC, " +
            "CASE WHEN LOWER(up.username) IS NOT NULL AND LOWER(up.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN 0 ELSE 1 END, " +
            "CASE WHEN LOWER(up.username) IS NOT NULL AND LOWER(up.username) LIKE LOWER(CONCAT('%', :searchTerm, '%')) THEN LOCATE(LOWER(:searchTerm), LOWER(up.username)) ELSE -1 END ASC")
    List<UserSearchResultDto> searchUsers(@Param("searchTerm") String searchTerm);







    Optional<User> findById(Long id);
}
