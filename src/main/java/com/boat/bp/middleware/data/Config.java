package com.boat.bp.middleware.data;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Config {
   
    private String PayloadURL;
    private String ContentType;
    


    public String getPayloadURL() {
        return PayloadURL;
    }

    @JsonSetter("Payload URL")
    public void setPayloadURL(String payloadURL) {
        PayloadURL = payloadURL;
    }
    public String getContentType() {
        return ContentType;
    }
    @JsonSetter("Content Type")
    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    

}
