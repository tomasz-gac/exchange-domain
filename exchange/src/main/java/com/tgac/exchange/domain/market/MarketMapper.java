package com.tgac.exchange.domain.market;

import com.tgac.exchange.domain.market.dto.MarketDto;
import com.tgac.exchange.domain.market.dto.TradeDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = PositionMapper.class)
public interface MarketMapper {
	MarketMapper INSTANCE = Mappers.getMapper(MarketMapper.class);

	MarketDto map(Market market);

	TradeDto map(Trade trade);
}
