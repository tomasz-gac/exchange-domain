package com.tgac.exchange.domain.order.dto;

import com.tgac.exchange.domain.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
	OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

	OrderDto orderToOrderDto(Order order);
}
