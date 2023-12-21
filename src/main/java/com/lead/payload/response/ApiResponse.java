package com.lead.payload.response;

public class ApiResponse {

    private String status;
    private Object data;

    public ApiResponse(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }
}
