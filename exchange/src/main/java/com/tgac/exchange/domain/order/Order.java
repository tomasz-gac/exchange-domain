package com.tgac.exchange.domain.order;

import com.tgac.exchange.domain.Money;
import com.tgac.exchange.domain.Price;
import com.tgac.exchange.domain.market.Trade;
import com.tgac.exchange.domain.market.TradeId;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Order implements AggregateRoot<Order, OrderId> {
	private final OrderId id;
	private final Instant createdAt;
	private final OrderSide orderSide;
	private final Price limitPrice;
	private final Money quantity;
	private final TimeInForce timeInForce;
	private OrderStatus status;
	private final List<Association<Trade, TradeId>> trades = new ArrayList<>();
}
