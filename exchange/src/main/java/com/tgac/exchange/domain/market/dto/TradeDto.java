package com.tgac.exchange.domain.market.dto;

import com.tgac.exchange.domain.Money;
import com.tgac.exchange.domain.Price;
import com.tgac.exchange.domain.market.PositionType;
import com.tgac.exchange.domain.market.TradeId;
import com.tgac.exchange.domain.order.Order;
import com.tgac.exchange.domain.order.OrderId;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jmolecules.ddd.types.Association;

@Value
@RequiredArgsConstructor
public class TradeDto {
	TradeId id;
	Association<Order, OrderId> taker;
	Association<Order, OrderId> maker;
	Price price;
	Money quantity;
	PositionType makerSide;
}
