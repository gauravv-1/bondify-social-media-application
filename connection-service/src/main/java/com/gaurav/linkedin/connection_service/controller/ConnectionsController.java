package com.gaurav.linkedin.connection_service.controller;


import com.gaurav.linkedin.connection_service.dto.ConnectionStatusDto;
import com.gaurav.linkedin.connection_service.entity.Person;
import com.gaurav.linkedin.connection_service.exceptions.ApiResponse;
import com.gaurav.linkedin.connection_service.service.ConnectionsService;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections(){
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections());
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> sendConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(new ApiResponse<>(connectionsService.sendConnectionRequest(userId)));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> acceptConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(new ApiResponse<>(connectionsService.acceptConnectionRequest(userId)));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> rejectConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(new ApiResponse<>(connectionsService.rejectConnectionRequest(userId)));
    }

    @PostMapping("/{userId}/getConnectionStatus")
    public ResponseEntity<ApiResponse<ConnectionStatusDto>> getConnectionStatus(@PathVariable Long userId){

        return ResponseEntity.ok(new ApiResponse<>(connectionsService.getConnectionStatus(userId)));
    }
}
