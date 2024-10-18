package com.tgac.exchange.domain;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.ValueObject;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class TypedAmount<T extends TypedAmount<T, U>, U extends Unit>
		implements ValueObject, Comparable<TypedAmount<T, U>> {
	private final BigDecimal value;
	private final U unit;

	protected abstract T instance(BigDecimal value, U unit);

	public T map(UnaryOperator<BigDecimal> f) {
		return instance(f.apply(value), unit);
	}

	public T flatMap(Function<BigDecimal, T> f) {
		return f.apply(value);
	}

	protected T apply(BinaryOperator<BigDecimal> op, T other) {
		verifySameUnit(other);
		return other.map(v -> op.apply(value, v));
	}

	public boolean isSameUnit(TypedAmount<T, U> other) {
		return unit.equals(other.getUnit());
	}

	protected void verifySameUnit(TypedAmount<T, U> other) {
		if (!isSameUnit(other)) {
			throw new IllegalArgumentException(
					String.format("Unit mismatch: %s and %s", unit, other.getUnit()));
		}
	}

	@Override
	public int compareTo(TypedAmount<T, U> o) {
		verifySameUnit(o);
		return value.compareTo(o.getValue());
	}

	@Override
	public String toString() {
		return value + "[" + unit + "]";
	}
}
