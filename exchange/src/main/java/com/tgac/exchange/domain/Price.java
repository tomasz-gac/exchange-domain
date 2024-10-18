package com.tgac.exchange.domain;

import java.math.BigDecimal;

public class Price extends TypedAmount<Price, Rate> {

	private Price(BigDecimal value, Rate unit) {
		super(value, unit);
	}

	public static Price price(BigDecimal value, Rate unit) {
		return new Price(value, unit);
	}

	@Override
	protected Price instance(BigDecimal value, Rate unit) {
		return new Price(value, unit);
	}
}
