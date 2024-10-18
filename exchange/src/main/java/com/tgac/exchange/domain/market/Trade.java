package com.tgac.exchange.domain.market;

import com.tgac.exchange.domain.Money;
import com.tgac.exchange.domain.Price;
import com.tgac.exchange.domain.order.Order;
import com.tgac.exchange.domain.order.OrderId;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;

@Value
@RequiredArgsConstructor
public class Trade implements AggregateRoot<Trade, TradeId> {
	TradeId id;
	Association<Order, OrderId> taker;
	Association<Order, OrderId> maker;
	Price price;
	Money quantity;
	PositionType makerSide;
}
