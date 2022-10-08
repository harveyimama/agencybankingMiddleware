package com.boat.bp.middleware.data;

import java.math.BigDecimal;
import lombok.Value;

@Value
public class Summary {
	private BigDecimal accountBalance;
	private BigDecimal availableBalance;
}
