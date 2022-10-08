package com.boat.bp.middleware.mifos.exception;

import com.boat.bp.middleware.responses.ResponseCode;
import com.boat.bp.middleware.responses.Result;

public class RestProcessingException extends RuntimeException {
    private ResponseCode responseCode;
    private Result<?> result;

    public RestProcessingException(ResponseCode responseCode) {
        super(responseCode.getDescription());
        this.responseCode = responseCode;
    }
    
    public RestProcessingException(Result<?> result) {
        super(result.getResponseMessage());
        this.responseCode = result.getResponseCode();
        this.result = result;
    }

    public RestProcessingException(String message, ResponseCode responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public RestProcessingException(Throwable cause, ResponseCode responseCode) {
        super(cause);
        this.responseCode = responseCode;
    }

    public RestProcessingException(String message, Throwable cause, ResponseCode responseCode) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public RestProcessingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace, ResponseCode responseCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return this.responseCode;
    }
    
    public Result<?> getResult() {
        return this.result;
    }
}
