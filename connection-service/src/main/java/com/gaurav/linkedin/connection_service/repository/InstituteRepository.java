package com.gaurav.linkedin.connection_service.repository;

import com.gaurav.linkedin.connection_service.entity.Institute;
import com.gaurav.linkedin.connection_service.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface InstituteRepository extends Neo4jRepository<Institute,Long> {
    Optional<Institute> findById(Long id);
}
