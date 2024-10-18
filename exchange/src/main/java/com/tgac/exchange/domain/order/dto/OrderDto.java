package com.tgac.exchange.domain.order.dto;

import com.tgac.exchange.domain.Money;
import com.tgac.exchange.domain.Price;
import com.tgac.exchange.domain.market.Trade;
import com.tgac.exchange.domain.market.TradeId;
import com.tgac.exchange.domain.order.OrderId;
import com.tgac.exchange.domain.order.OrderSide;
import com.tgac.exchange.domain.order.OrderStatus;
import com.tgac.exchange.domain.order.TimeInForce;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import org.jmolecules.ddd.types.Association;

@Value
@Builder
public class OrderDto {
	OrderId id;
	Instant createdAt;
	OrderSide orderSide;
	Price limitPrice;
	Money quantity;
	TimeInForce timeInForce;
	OrderStatus status;
	List<Association<Trade, TradeId>> trades = new ArrayList<>();
}
