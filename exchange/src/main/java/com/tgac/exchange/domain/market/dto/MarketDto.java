package com.tgac.exchange.domain.market.dto;

import com.tgac.exchange.domain.market.Instrument;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class MarketDto {
	Instrument instrument;
	List<PositionDto> asks = new ArrayList<>();
	List<PositionDto> bids = new ArrayList<>();
}
