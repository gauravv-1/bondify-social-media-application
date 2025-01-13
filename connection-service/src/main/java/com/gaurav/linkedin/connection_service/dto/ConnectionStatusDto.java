package com.gaurav.linkedin.connection_service.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ConnectionStatusDto {

    private Boolean isConnected;
    private Boolean isRequested;

}
