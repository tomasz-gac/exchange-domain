package com.tgac.exchange.domain.market;

import com.tgac.exchange.domain.Currency;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.jmolecules.ddd.types.Identifier;

@Value
@RequiredArgsConstructor(staticName = "instrument")
public class Instrument implements Identifier {
	private static final String SEPARATOR = "-";
	Currency base;
	Currency quote;

	@Override
	public String toString() {
		return base + SEPARATOR + quote;
	}
}
