package com.tgac.exchange.domain;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "currency")
public class Currency implements Unit {
	String name;

	@Override
	public String toString() {
		return name;
	}

	public Money amountOf(BigDecimal amount) {
		return Money.of(amount, this);
	}
}
