package com.tgac.exchange.domain.wallet;

import lombok.Getter;
import org.jmolecules.ddd.types.Identifier;

@Getter
public class InsufficientFundsException extends Exception {
	private final Identifier id;

	public InsufficientFundsException(Identifier id) {
		super(String.format("Insufficient funds for balance %s", id));
		this.id = id;
	}
}
