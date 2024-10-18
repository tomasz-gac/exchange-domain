package com.tgac.exchange.domain.market;

import com.tgac.exchange.domain.order.OrderStatus;
import com.tgac.exchange.domain.order.TimeInForce;
import com.tgac.exchange.domain.order.dto.OrderDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.jmolecules.ddd.types.AggregateRoot;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Market implements AggregateRoot<Market, Instrument> {
	private final Instrument instrument;
	private final List<Position> asks = new ArrayList<>();
	private final List<Position> bids = new ArrayList<>();

	@Override
	public Instrument getId() {
		return instrument;
	}

	public List<Position> getBids() {
		return Collections.unmodifiableList(bids);
	}

	public List<Position> getAsks() {
		return Collections.unmodifiableList(asks);
	}

	public List<Trade> execute(OrderDto order) {
		if (order.getStatus() != OrderStatus.NEW) {
			throw new IllegalArgumentException(String.format("Cannot match orders with state %s", order.getOrderSide()));
		}
		Position taker = Position.fromOrder(order);

		List<Match> matches;
		switch (order.getOrderSide()) {
			case BUY:
				matches = matchBuy(taker);
				break;
			case SELL:
				matches = matchSell(taker);
				break;
			default:
				throw new UnsupportedOperationException("Unsupported order type: " + order);
		}

		if (order.getTimeInForce() == TimeInForce.IMMEDIATE_OR_FAIL) {
			if (taker.getSize().getValue().compareTo(BigDecimal.ZERO) != 0) {
				return Collections.emptyList();
			}
		}
		matches.forEach(m ->
				m.getPosition().execute(m.getTrade()));

		addPositionAndClean(taker);

		return matches.stream()
				.map(Match::getTrade)
				.collect(Collectors.toList());
	}

	private List<Match> matchBuy(Position taker) {
		List<Match> trades = new ArrayList<>();

		for (Position ask : asks) {
			Optional<Trade> potentialTrade = ask.match(taker);
			if (!potentialTrade.isPresent()) {
				break;
			}
			Trade trade = potentialTrade.get();
			taker.execute(trade);
			trades.add(Match.of(ask, trade));
		}
		return trades;
	}

	private List<Match> matchSell(Position taker) {
		List<Match> matches = new ArrayList<>();

		for (Position bid : bids) {
			Optional<Trade> potentialTrade = bid.match(taker);
			if (!potentialTrade.isPresent()) {
				break;
			}
			Trade trade = potentialTrade.get();
			taker.execute(trade);
			matches.add(Match.of(bid, trade));
		}
		return matches;
	}

	private void addPositionAndClean(Position taker) {
		if (taker.getSize().getValue().signum() > 0) {
			if (taker.getType() == PositionType.BID) {
				bids.add(taker);
			} else {
				asks.add(taker);
			}
		}

		asks.removeIf(p -> p.getSize().getValue().compareTo(BigDecimal.ZERO) == 0);
		asks.sort(Comparator.comparing(Position::getPrice)
				.thenComparing(Position::getCreatedAt)
				.thenComparing(Position::getSize));

		bids.removeIf(p -> p.getSize().getValue().compareTo(BigDecimal.ZERO) == 0);
		bids.sort(Comparator.comparing(Position::getPrice).reversed()
				.thenComparing(Position::getCreatedAt)
				.thenComparing(Position::getSize));

	}

	@Value
	@RequiredArgsConstructor(staticName = "of")
	private static class Match {
		Position position;
		Trade trade;
	}
}
