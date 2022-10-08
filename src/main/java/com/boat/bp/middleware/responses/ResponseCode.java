package com.boat.bp.middleware.responses;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Response code can give insight on where and why a process failed
 */
public enum ResponseCode {
    SUCCESS(0, "Successful"), 
    ERROR(99, "Unknown error"), 
    
    // ---------- Onboarding related response code, 11x errors

    /**
     * 110 -- Onboarding request failed.
     */
    ONBARDING_ERROR(110, "Onboarding request failed"),

    /**
     * 111 - Client was created successfully, but account opening failed
     */
    ONBOARDING_ERROR_AO(111, "Client was created successfully, but account opening failed"),

    /**
     * 112 -- Onboarding: create client and account opening were successful, but account approval failed
     */
    ONBOARDING_ERROR_AA(112, "Create client and account opening were successful, but account approval failed"),

    /**
     * 113 -- Onboarding: create client, open account, approve account were successful, but activate account failed.
     */
    ONBOARDING_ERROR_AC(113, "create client, open account, approve account were successful, but activate account failed"),

    ACCOUNT_APPROVAL_ERROR(12, "Account approval failed"),
    ACCOUNT_ACTIVATION_ERROR(13, "Account activation failed");

    private int code;
    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public int getCode() {
        return this.code;
    }
    
    public String getDescription() {
        return this.description;
    }
}
