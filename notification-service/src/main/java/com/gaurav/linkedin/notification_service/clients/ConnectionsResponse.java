package com.gaurav.linkedin.notification_service.clients;

import com.gaurav.linkedin.notification_service.dto.Person;
import lombok.Data;

import java.util.List;

@Data
public class ConnectionsResponse {
    private List<Person> connections;
}
