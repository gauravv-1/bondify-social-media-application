package com.gaurav.linkedin.notification_service.clients;

import lombok.Data;

import java.util.List;

@Data
public class WrappedResponse<T> {
    private String status;
    private List<T> data;
}
