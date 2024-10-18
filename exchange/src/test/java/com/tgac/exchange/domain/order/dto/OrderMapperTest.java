package com.tgac.exchange.domain.order.dto;

import static com.tgac.exchange.domain.market.OrderFactory.createOrder;
import static org.assertj.core.api.Assertions.assertThat;

import com.tgac.exchange.domain.order.Order;
import com.tgac.exchange.domain.order.OrderSide;
import com.tgac.exchange.domain.order.TimeInForce;
import org.junit.Test;

public class OrderMapperTest {

	@Test
	public void shouldMap() {
		Order order = createOrder(OrderSide.SELL, TimeInForce.IMMEDIATE_OR_FAIL, 10, 10);
		OrderDto dto = OrderMapper.INSTANCE.orderToOrderDto(order);

		assertThat(dto.getId())
				.isEqualTo(order.getId());
		assertThat(dto.getOrderSide())
				.isEqualTo(order.getOrderSide());
		assertThat(dto.getQuantity())
				.isEqualByComparingTo(order.getQuantity());
		assertThat(dto.getCreatedAt())
				.isEqualTo(order.getCreatedAt());
	}
}