package com.boat.bp.middleware.mifos;

import lombok.Value;

@Value
public class CreateClientResponseValue {
    private final String officeId;
    private final String clientId;
    private final String resourceId;
    private final String savingsId;
}
