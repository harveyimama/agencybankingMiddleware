package com.boat.bp.middleware.mifos.value.response;

import lombok.Value;

@Value
public class NewAccountResponseValue {
    private final String officeId;
    private final String clientId;
    private final String resourceId;
    private final String savingsId;
}
