package com.tgac.exchange.domain;

import java.math.BigDecimal;

public class Money extends TypedAmount<Money, Currency> {

	private Money(BigDecimal value, Currency currency) {
		super(value, currency);
	}

	public static Money of(BigDecimal value, Currency currency) {
		return new Money(value, currency);
	}

	public static Money zero(Currency currency) {
		return new Money(BigDecimal.ZERO, currency);
	}

	@Override
	protected Money instance(BigDecimal value, Currency unit) {
		return new Money(value, unit);
	}

	public Money add(Money other) {
		return apply(BigDecimal::add, other);
	}

	public Money subtract(Money other) {
		return apply(BigDecimal::subtract, other);
	}
}
