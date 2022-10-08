package com.boat.bp.middleware.mifos.value.response;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value @Builder @Jacksonized
public class OnboardingResponse {
    // always true since create account is called on user activation
    private String clientId;
    private boolean activated;
    private String userStatus;
    private String savingsAccountId;
    private String commissionAccountId;
    private String walletAccountId;
    private String loanAccountId;
}
