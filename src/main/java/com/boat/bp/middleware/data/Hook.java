package com.boat.bp.middleware.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.responses.GeneralResponse;

public class Hook<T> {
    private String createdByName;
    private T request;
    private String entityName;
    private GeneralResponse<?> response;
    private String createdByFullName;
    private String actionName;
    private String timestamp;

    public Hook(T request, GeneralResponse<?> response) {
        this.request = request;
        this.response = response;
        this.entityName ="ACCOUNTTRANSFER";
        this.actionName = "CREATE";
        Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat(Settings.DATE_FORMAT);
		this.timestamp = dateFormat.format(date);
    } 

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public T getRequest() {
        return request;
    }

    public void setRequest(T request) {
        this.request = request;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public GeneralResponse<?> getResponse() {
        return response;
    }

    public void setResponse(GeneralResponse<?> response) {
        this.response = response;
    }

    public String getCreatedByFullName() {
        return createdByFullName;
    }

    public void setCreatedByFullName(String createdByFullName) {
        this.createdByFullName = createdByFullName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    
    
}
