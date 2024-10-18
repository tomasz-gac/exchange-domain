package com.tgac.exchange.domain.market;

import static com.tgac.exchange.domain.market.OrderFactory.BTC;
import static com.tgac.exchange.domain.market.OrderFactory.USD;
import static com.tgac.exchange.domain.market.OrderFactory.createOrderDto;
import static com.tgac.exchange.domain.market.PositionType.ASK;
import static com.tgac.exchange.domain.market.PositionType.BID;
import static com.tgac.exchange.domain.order.OrderSide.BUY;
import static com.tgac.exchange.domain.order.OrderSide.SELL;
import static org.assertj.core.api.Assertions.assertThat;

import com.tgac.exchange.domain.Rate;
import com.tgac.exchange.domain.order.TimeInForce;
import com.tgac.exchange.domain.order.dto.OrderDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class MarketTest {
	private final Market btcUsd = new Market(Instrument.instrument(BTC, USD));

	@Test
	public void shouldAddPosition() {
		OrderDto order = createOrderDto(BUY, 10, 100);
		List<Trade> trades = btcUsd.execute(order);

		assertThat(trades).isEmpty();

		assertThat(btcUsd.getAsks()).isEmpty();
		assertThat(btcUsd.getBids()).hasSize(1);
		assertThat(btcUsd.getBids().get(0))
				.matches(p -> p.getPrice().compareTo(order.getLimitPrice()) == 0)
				.matches(p -> p.getSize().compareTo(order.getQuantity()) == 0)
				.matches(p -> p.getOrder().getId().equals(order.getId()));
	}

	@Test
	public void shouldMatchSellOrder() {
		prepareMarket();

		OrderDto order = createOrderDto(SELL, 10, 5);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(1);
		assertTrade(trades.get(0), BID, 10, 5);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(3);
		assertAsk(btcUsd, 2, 20, 15);
		assertAsk(btcUsd, 1, 17, 20);
		assertAsk(btcUsd, 0, 15, 10);

		assertThat(btcUsd.getBids()).hasSize(3);
		assertBid(btcUsd, 0, 10, 5);
		assertBid(btcUsd, 1, 7, 5);
		assertBid(btcUsd, 2, 5, 20);
	}

	@Test
	public void shouldMatchSellOrderManyLevels() {
		prepareMarket();

		OrderDto order = createOrderDto(SELL, 6, 15);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(2);
		assertTrade(trades.get(0), BID, 10, 10);
		assertTrade(trades.get(1), BID, 7, 5);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(3);
		assertAsk(btcUsd, 2, 20, 15);
		assertAsk(btcUsd, 1, 17, 20);
		assertAsk(btcUsd, 0, 15, 10);

		assertThat(btcUsd.getBids()).hasSize(1);
		assertBid(btcUsd, 0, 5, 20);
	}

	@Test
	public void shouldMatchSellOrderManyLevelsAndLeavePosition() {
		prepareMarket();

		OrderDto order = createOrderDto(SELL, 6, 25);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(2);
		assertTrade(trades.get(0), BID, 10, 10);
		assertTrade(trades.get(1), BID, 7, 5);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(4);
		assertAsk(btcUsd, 3, 20, 15);
		assertAsk(btcUsd, 2, 17, 20);
		assertAsk(btcUsd, 1, 15, 10);
		assertAsk(btcUsd, 0, 6, 10);

		assertThat(btcUsd.getBids()).hasSize(1);
		assertBid(btcUsd, 0, 5, 20);
	}

	@Test
	public void shouldMatchBuyOrder() {
		prepareMarket();

		OrderDto order = createOrderDto(BUY, 15, 5);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(1);
		assertTrade(trades.get(0), ASK, 15, 5);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(3);
		assertAsk(btcUsd, 2, 20, 15);
		assertAsk(btcUsd, 1, 17, 20);
		assertAsk(btcUsd, 0, 15, 5);

		assertThat(btcUsd.getBids()).hasSize(3);
		assertBid(btcUsd, 0, 10, 10);
		assertBid(btcUsd, 1, 7, 5);
		assertBid(btcUsd, 2, 5, 20);
	}

	@Test
	public void shouldMatchBuyOrderManyLevels() {
		prepareMarket();

		OrderDto order = createOrderDto(BUY, 18, 15);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(2);
		assertTrade(trades.get(0), ASK, 15, 10);
		assertTrade(trades.get(1), ASK, 17, 5);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(2);
		assertAsk(btcUsd, 1, 20, 15);
		assertAsk(btcUsd, 0, 17, 15);

		assertThat(btcUsd.getBids()).hasSize(3);
		assertBid(btcUsd, 0, 10, 10);
		assertBid(btcUsd, 1, 7, 5);
		assertBid(btcUsd, 2, 5, 20);
	}

	@Test
	public void shouldMatchBuyOrderManyLevelsAndLeavePosition() {
		prepareMarket();

		OrderDto order = createOrderDto(BUY, 18, 35);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(2);
		assertTrade(trades.get(0), ASK, 15, 10);
		assertTrade(trades.get(1), ASK, 17, 20);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(1);
		assertAsk(btcUsd, 0, 20, 15);

		assertThat(btcUsd.getBids()).hasSize(4);
		assertBid(btcUsd, 0, 18, 5);
		assertBid(btcUsd, 1, 10, 10);
		assertBid(btcUsd, 2, 7, 5);
		assertBid(btcUsd, 3, 5, 20);
	}

	@Test
	public void shouldMatchSellOrderImmediateOrFail() {
		prepareMarket();

		OrderDto order = createOrderDto(SELL, TimeInForce.IMMEDIATE_OR_FAIL, 10, 5);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(1);
		assertTrade(trades.get(0), BID, 10, 5);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(3);
		assertAsk(btcUsd, 2, 20, 15);
		assertAsk(btcUsd, 1, 17, 20);
		assertAsk(btcUsd, 0, 15, 10);

		assertThat(btcUsd.getBids()).hasSize(3);
		assertBid(btcUsd, 0, 10, 5);
		assertBid(btcUsd, 1, 7, 5);
		assertBid(btcUsd, 2, 5, 20);
	}

	@Test
	public void shouldNotMatchSellOrderImmediateOrFailWhenNotFilled() {
		prepareMarket();

		OrderDto order = createOrderDto(SELL, TimeInForce.IMMEDIATE_OR_FAIL, 10, 15);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).isEmpty();
	}

	@Test
	public void shouldMatchBuyOrderImmediateOrFail() {
		prepareMarket();

		OrderDto order = createOrderDto(BUY, TimeInForce.IMMEDIATE_OR_FAIL, 15, 5);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).hasSize(1);
		assertTrade(trades.get(0), ASK, 15, 5);

		System.out.println(trades);
		printOrderBook(btcUsd);

		assertThat(btcUsd.getAsks()).hasSize(3);
		assertAsk(btcUsd, 2, 20, 15);
		assertAsk(btcUsd, 1, 17, 20);
		assertAsk(btcUsd, 0, 15, 5);

		assertThat(btcUsd.getBids()).hasSize(3);
		assertBid(btcUsd, 0, 10, 10);
		assertBid(btcUsd, 1, 7, 5);
		assertBid(btcUsd, 2, 5, 20);
	}

	@Test
	public void shouldNotMatchBuyOrderImmediateOrFailWhenNotFilled() {
		prepareMarket();

		OrderDto order = createOrderDto(BUY, TimeInForce.IMMEDIATE_OR_FAIL, 15, 15);

		List<Trade> trades = btcUsd.execute(order);
		assertThat(trades).isEmpty();
	}

	private static void assertTrade(Trade trade, PositionType side, double price, double quantity) {
		assertThat(trade.getMakerSide())
				.isEqualTo(side);
		assertThat(trade.getPrice().getValue())
				.isEqualByComparingTo(BigDecimal.valueOf(price));
		assertThat(trade.getQuantity().getValue())
				.isEqualByComparingTo(BigDecimal.valueOf(quantity));
	}

	private static void assertBid(Market market, int level, double price, double size) {
		Position position = market.getBids().get(level);
		assertThat(position.getType()).isEqualTo(BID);
		assertPosition(position, price, size);
	}

	private static void assertAsk(Market market, int level, double price, double size) {
		Position position = market.getAsks().get(level);
		assertThat(position.getType()).isEqualTo(PositionType.ASK);
		assertPosition(position, price, size);
	}

	private static void assertPosition(Position position, double price, double size) {
		assertThat(position.getPrice().getValue())
				.isEqualByComparingTo(BigDecimal.valueOf(price));
		assertThat(position.getSize().getValue())
				.isEqualByComparingTo(BigDecimal.valueOf(size));
	}

	@Test
	public void shouldEqualItself() {
		assertThat(Rate.rate(BTC, USD).equals(Rate.rate(BTC, USD))).isTrue();
	}

	private void prepareMarket() {
		btcUsd.execute(createOrderDto(SELL, 20, 15));
		btcUsd.execute(createOrderDto(SELL, 17, 20));
		btcUsd.execute(createOrderDto(SELL, 15, 10));

		btcUsd.execute(createOrderDto(BUY, 10, 10));
		btcUsd.execute(createOrderDto(BUY, 7, 5));
		btcUsd.execute(createOrderDto(BUY, 5, 20));
	}

	private static void printOrderBook(Market market) {
		ArrayList<Position> sellers = new ArrayList<>(market.getAsks());
		Collections.reverse(sellers);
		sellers.forEach(ask ->
				System.out.printf("%s@%s%n", ask.getSize(), ask.getPrice()));
		System.out.println();
		market.getBids().forEach(bid ->
				System.out.printf("%s@%s%n", bid.getSize(), bid.getPrice()));
		System.out.println();
	}
}