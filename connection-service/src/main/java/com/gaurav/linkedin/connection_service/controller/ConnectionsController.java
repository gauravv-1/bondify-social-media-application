package com.gaurav.linkedin.connection_service.controller;


import com.gaurav.linkedin.connection_service.dto.ConnectionStatusDto;
import com.gaurav.linkedin.connection_service.entity.Person;
import com.gaurav.linkedin.connection_service.exceptions.ApiResponse;
import com.gaurav.linkedin.connection_service.service.ConnectionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.apiguardian.api.API;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/first-degree")
    public ResponseEntity<List<Person>> getFirstConnections(@RequestHeader("X-User-Id") Long userId){
        return ResponseEntity.ok(connectionsService.getFirstDegreeConnections(userId));
    }

    @PostMapping("/request/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> sendConnectionRequest(@PathVariable Long userId, HttpServletRequest request){
        return ResponseEntity.ok(new ApiResponse<>(connectionsService.sendConnectionRequest(userId,request)));
    }

    @PostMapping("/accept/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> acceptConnectionRequest(@PathVariable Long userId,HttpServletRequest request){
        return ResponseEntity.ok(new ApiResponse<>(connectionsService.acceptConnectionRequest(userId, request)));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> rejectConnectionRequest(@PathVariable Long userId){
        return ResponseEntity.ok(new ApiResponse<>(connectionsService.rejectConnectionRequest(userId)));
    }

    @PostMapping("/{userId}/getConnectionStatus")
    public ResponseEntity<ApiResponse<ConnectionStatusDto>> getConnectionStatus(@PathVariable Long userId){

        return ResponseEntity.ok(new ApiResponse<>(connectionsService.getConnectionStatus(userId)));
    }

    @GetMapping("/getConnectedUserId")
    public List<Long> getConnectedUserId(){
        List<Long> connectedIds = connectionsService.getConnectedUserId();
        return connectedIds;
    }
}
