package com.tgac.exchange.domain.market;

import com.tgac.exchange.domain.market.dto.PositionDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface PositionMapper {
	PositionDto map(Position position);
}
