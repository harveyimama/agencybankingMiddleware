package com.boat.bp.middleware.mifos.value.response;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class AccountBalanceValue {
    private String id;
    private String clientId;
    private String productId;
    private String dateFormat;
    private String locale;
    private String submittedOnDate;
    private String accountNo;
    private String savingsProductName;
    private Summary summary;

    @Value
    public static class Summary {
        private final BigDecimal accountBalance;
        private final BigDecimal availableBalance;
    }
}
