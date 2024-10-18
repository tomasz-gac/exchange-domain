package com.tgac.exchange.domain.market.dto;

import com.tgac.exchange.domain.Money;
import com.tgac.exchange.domain.Price;
import com.tgac.exchange.domain.market.PositionId;
import com.tgac.exchange.domain.market.PositionType;
import com.tgac.exchange.domain.order.Order;
import com.tgac.exchange.domain.order.OrderId;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jmolecules.ddd.types.Association;

@Value
@RequiredArgsConstructor
public class PositionDto {
	PositionId id;
	PositionType type;
	Instant createdAt;
	Association<Order, OrderId> order;
	Price price;
	Money size;

}
