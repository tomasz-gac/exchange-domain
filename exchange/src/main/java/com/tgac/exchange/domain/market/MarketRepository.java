package com.tgac.exchange.domain.market;

public interface MarketRepository {
	Market findByInstrument(Instrument instrument);

	Trade findById(TradeId tradeId);
}
