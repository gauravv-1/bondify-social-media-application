package com.gaurav.linkedin.connection_service.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
public class Institute {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String location;
    private Long numberOfStudents;
    private boolean studentRepresentative;
}
