package com.boat.bp.middleware.responses;

import lombok.Value;

@Value
public class Result<D> {
    
    private ResponseCode responseCode;
    private String responseMessage;
    private D data;

    public boolean isSuccessful() {
        return responseCode == ResponseCode.SUCCESS;
    }

    public static <D> Result<D> successful() {
        return new Result<D>(ResponseCode.SUCCESS, "Successful", null);
    }

    public static <D> Result<D> successful(D data) {
        return new Result<D>(ResponseCode.SUCCESS, "Successful", data);
    }

    public static <D> Result<D> successful(D data, String message) {
        return new Result<D>(ResponseCode.SUCCESS, message, data);
    }

    public static <D> Result<D> failed() {
        return new Result<D>(ResponseCode.ERROR, "Failed", null);
    }

    public static <D> Result<D> failed(String message) {
        return new Result<D>(ResponseCode.ERROR, message, null);
    }

    public static <D> Result<D> failed(ResponseCode responseCode) {
        return new Result<D>(responseCode, "Failed", null);
    }

    public static <D> Result<D> failed(ResponseCode responseCode, String message) {
        return new Result<D>(responseCode, message, null);
    }
}
