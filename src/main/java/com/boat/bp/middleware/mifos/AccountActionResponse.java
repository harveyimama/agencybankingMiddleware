package com.boat.bp.middleware.mifos;

import lombok.Value;

@Value
public class AccountActionResponse {
    private final String officeId;
    private final String clientId;
    private final String savingsId;
    private final String resourceId;
    private final String locale;
    private final String dateFormat;
    private final String approvedOnDate;
    private final Changes changes;

    @Value
    static class Changes {
        private Status status;
    }

    @Value
    static class Status {
        private final String id;
        private final String code;
        private final String value;
        private final String submittedAndPendingApproval;
        private final String approved;
        private final String rejected;
        private final String withdrawnByApplicant;
        private final boolean active;
        private final boolean closed;
    }
}
