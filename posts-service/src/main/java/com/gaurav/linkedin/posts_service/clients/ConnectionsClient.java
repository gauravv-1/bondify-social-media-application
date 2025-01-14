package com.gaurav.linkedin.posts_service.clients;

import com.gaurav.linkedin.posts_service.dto.PersonDto;
import com.gaurav.linkedin.posts_service.exceptions.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "connection-service",path = "/connections",url = "${CONNECTION_SERVICE_URI:}")
public interface ConnectionsClient {

    @GetMapping("/core/first-degree")
    List<PersonDto> getFirstConnections();

    @GetMapping("/core/getConnectedUserId")
    ApiResponse<List<Long>> getConnectedUserId();


}
