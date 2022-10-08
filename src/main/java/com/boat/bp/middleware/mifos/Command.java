package com.boat.bp.middleware.mifos;

public enum Command {
    APPROVE("approve"), ACTIVATE("activate");

    private String value;

    private Command(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }    
}
