package com.boat.bp.middleware.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Callback {
    private String name;
    private boolean isActive; 
    private String displayName;
    private List<Event> events;
    private Config config;

    public Callback(String callBackURL) {
        this.name = "Web";
        this.isActive = true;
        this.displayName = "Account Transfer Hook";
        this.config = new Config();
        this.config .setContentType("json");
        this.config.setPayloadURL(callBackURL);
        this.events = new  ArrayList<Event>();
        this.events.add(new Event("CREATE","ACCOUNTTRANSFER"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }
    @JsonSetter("isActive")
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    
    
}
