package com.gaurav.linkedin.connection_service.controller;


import com.gaurav.linkedin.connection_service.dto.InstituteDto;
import com.gaurav.linkedin.connection_service.exceptions.ApiResponse;
import com.gaurav.linkedin.connection_service.service.ConnectionsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/institute")
@RequiredArgsConstructor
@Slf4j
public class InstituteController {

    private final ConnectionsService connectionsService;
    @GetMapping("/getAllInstitutes")
    public ResponseEntity<ApiResponse<List<InstituteDto>>> getAllInstitutes(HttpServletRequest httpServletRequest){
        log.info("At getAllInstitutes Method");
        List<InstituteDto> institutes = connectionsService.getAllInstitutes(httpServletRequest);
        return new ResponseEntity<>(new ApiResponse<>(institutes),HttpStatus.OK);
    }
}
