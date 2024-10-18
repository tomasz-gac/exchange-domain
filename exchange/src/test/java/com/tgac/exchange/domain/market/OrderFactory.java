package com.tgac.exchange.domain.market;

import static com.tgac.exchange.domain.Price.price;

import com.tgac.exchange.domain.Currency;
import com.tgac.exchange.domain.Money;
import com.tgac.exchange.domain.Rate;
import com.tgac.exchange.domain.order.Order;
import com.tgac.exchange.domain.order.OrderId;
import com.tgac.exchange.domain.order.OrderSide;
import com.tgac.exchange.domain.order.OrderStatus;
import com.tgac.exchange.domain.order.TimeInForce;
import com.tgac.exchange.domain.order.dto.OrderDto;
import com.tgac.exchange.domain.order.dto.OrderMapper;
import java.math.BigDecimal;
import java.time.Instant;
import org.jmolecules.ddd.types.Association;

public class OrderFactory {
	public static final Currency BTC = Currency.currency("BTC");
	public static final Currency USD = Currency.currency("USD");
	public static final Rate RATE = Rate.rate(BTC, USD);

	public static OrderDto createOrderDto(OrderSide side, double price, double size) {
		return createOrderDto(side, TimeInForce.GOOD_TILL_CANCEL, price, size);
	}

	public static OrderDto createOrderDto(OrderSide side, TimeInForce time, double price, double size) {
		return OrderMapper.INSTANCE.orderToOrderDto(
				createOrder(side, time, price, size));
	}

	public static Order createOrder(OrderSide side, double price, double size) {
		return createOrder(side, TimeInForce.GOOD_TILL_CANCEL, price, size);
	}

	public static Order createOrder(OrderSide side, TimeInForce time, double price, double size) {
		return new Order(new OrderId(),
				Instant.now(),
				side,
				price(BigDecimal.valueOf(price), RATE),
				Money.of(BigDecimal.valueOf(size), USD),
				time,
				OrderStatus.NEW);
	}

	public static Position createPosition(PositionType side, double price, double size) {
		return new Position(new PositionId(),
				side,
				Instant.now(),
				Association.forId(new OrderId()),
				price(BigDecimal.valueOf(price), RATE),
				Money.of(BigDecimal.valueOf(size), USD));
	}
}
