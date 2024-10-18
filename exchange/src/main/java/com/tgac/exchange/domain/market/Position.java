package com.tgac.exchange.domain.market;

import com.tgac.exchange.domain.Money;
import com.tgac.exchange.domain.Price;
import com.tgac.exchange.domain.order.Order;
import com.tgac.exchange.domain.order.OrderId;
import com.tgac.exchange.domain.order.dto.OrderDto;
import java.time.Instant;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Entity;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
class Position implements Entity<Market, PositionId> {
	private final PositionId id;
	private final PositionType type;
	private final Instant createdAt;
	private final Association<Order, OrderId> order;
	private final Price price;
	private Money size;

	static Position fromOrder(OrderDto order) {
		return new Position(
				new PositionId(),
				PositionType.from(order.getOrderSide()),
				order.getCreatedAt(),
				Association.forId(order.getId()),
				order.getLimitPrice(),
				order.getQuantity());
	}

	public Optional<Trade> match(Position taker) {
		if (!positionsMatchByType(taker)) {
			throw new IllegalArgumentException("Cannot match position " + taker + " with " + this);
		}
		switch (taker.getType()) {
			case BID:
				return matchThisAskWithBid(taker);
			case ASK:
				return matchThisBidWithAsk(taker);
			default:
				throw new UnsupportedOperationException("Unsupported order side: " + taker);
		}
	}

	public void execute(Trade trade) {
		if (!tradeMatchesThisPosition(trade)) {
			throw new IllegalArgumentException("Trade " + trade + " does not match position " + this);
		}
		size = size.subtract(trade.getQuantity());
	}

	private boolean tradeMatchesThisPosition(Trade trade) {
		return trade.getMaker().pointsTo(order.getId()) ||
				trade.getTaker().pointsTo(order.getId());
	}

	private Optional<Trade> matchThisAskWithBid(Position other) {
		if (price.compareTo(other.getPrice()) <= 0) {
			return Optional.of(createTrade(other));
		} else {
			return Optional.empty();
		}
	}

	private Optional<Trade> matchThisBidWithAsk(Position order) {
		if (price.compareTo(order.getPrice()) >= 0) {
			return Optional.of(createTrade(order));
		} else {
			return Optional.empty();
		}
	}

	private Trade createTrade(Position other) {
		return new Trade(
				new TradeId(),
				other.getOrder(),
				order,
				price,
				other.getSize().compareTo(size) <= 0 ?
						other.getSize() :
						size,
				type);
	}

	private boolean positionsMatchByType(Position position) {
		return type != position.getType();
	}

}
