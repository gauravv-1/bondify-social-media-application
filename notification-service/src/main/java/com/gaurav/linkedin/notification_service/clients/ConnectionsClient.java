package com.gaurav.linkedin.notification_service.clients;


import com.gaurav.linkedin.notification_service.dto.Person;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connections")
public interface ConnectionsClient {

    @GetMapping("/core/first-degree")
    WrappedResponse<Person> getFirstConnections(@RequestHeader("X-User-Id") Long userId);

//    @GetMapping("/core/first-degree")
//    List<PersonDto> getFirstConnections(@RequestHeader("X-User-Id") Long userId);


}
