package com.tgac.exchange.domain.market;

import com.tgac.exchange.domain.order.OrderSide;

public enum PositionType {
	BID, ASK;

	public static PositionType from(OrderSide side) {
		return side == OrderSide.SELL ? ASK : BID;
	}
}
