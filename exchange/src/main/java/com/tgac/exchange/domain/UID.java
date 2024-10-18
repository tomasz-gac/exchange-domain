package com.tgac.exchange.domain;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.types.Identifier;

@Getter
@ToString
@EqualsAndHashCode
public abstract class UID implements Identifier {
	private final UUID id;

	protected UID() {
		id = UUID.randomUUID();
	}

	protected UID(UUID id) {
		this.id = id;
	}
}
