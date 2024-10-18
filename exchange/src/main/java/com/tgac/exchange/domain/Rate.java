package com.tgac.exchange.domain;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(staticName = "rate")
public class Rate implements Unit {
	Currency base;
	Currency quote;

	@Override
	public String toString() {
		return base + "/" + quote;
	}

	public Price valueOf(BigDecimal value) {
		return Price.price(value, this);
	}
}
