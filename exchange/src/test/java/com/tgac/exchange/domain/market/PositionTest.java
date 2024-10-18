package com.tgac.exchange.domain.market;

import static com.tgac.exchange.domain.market.OrderFactory.RATE;
import static com.tgac.exchange.domain.market.OrderFactory.USD;
import static com.tgac.exchange.domain.market.OrderFactory.createPosition;
import static org.assertj.core.api.Assertions.assertThat;

import com.tgac.exchange.domain.Money;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.Test;

public class PositionTest {

	@Test
	public void shouldMatchBuyWhenPriceIsLower() {
		Position position = createPosition(PositionType.ASK, 9, 100);
		Position order = createPosition(PositionType.BID, 10, 10);

		Optional<Trade> trade = position.match(order);

		assertThat(trade)
				.hasValueSatisfying(t -> assertThat(t.getMakerSide()).isEqualTo(PositionType.ASK))
				.hasValueSatisfying(t -> assertThat(t.getQuantity())
						.isEqualByComparingTo(USD.amountOf(BigDecimal.TEN)))
				.hasValueSatisfying(t -> assertThat(t.getPrice())
						.isEqualByComparingTo(RATE.valueOf(BigDecimal.valueOf(9))));

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(100)));

		trade.ifPresent(position::execute);

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(90)));
	}

	@Test
	public void shouldMatchBuyWhenPriceIsEqual() {
		Position position = createPosition(PositionType.ASK, 10, 100);
		Position order = createPosition(PositionType.BID, 10, 10);

		Optional<Trade> trade = position.match(order);

		assertThat(trade)
				.hasValueSatisfying(t -> assertThat(t.getMakerSide()).isEqualTo(PositionType.ASK))
				.hasValueSatisfying(t -> assertThat(t.getQuantity())
						.isEqualByComparingTo(USD.amountOf(BigDecimal.TEN)))
				.hasValueSatisfying(t -> assertThat(t.getPrice())
						.isEqualByComparingTo(RATE.valueOf(BigDecimal.valueOf(10))));

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(100)));

		trade.ifPresent(position::execute);

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(90)));
	}

	@Test
	public void shouldMatchBuyWhenPriceIsLowerAndEatWholePosition() {
		Position position = createPosition(PositionType.ASK, 9, 10);
		Position order = createPosition(PositionType.BID, 10, 100);

		Optional<Trade> trade = position.match(order);

		assertThat(trade)
				.hasValueSatisfying(t -> assertThat(t.getMakerSide()).isEqualTo(PositionType.ASK))
				.hasValueSatisfying(t -> assertThat(t.getQuantity())
						.isEqualByComparingTo(USD.amountOf(BigDecimal.TEN)))
				.hasValueSatisfying(t -> assertThat(t.getPrice())
						.isEqualByComparingTo(RATE.valueOf(BigDecimal.valueOf(9))));

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(10)));

		trade.ifPresent(position::execute);

		assertThat(position.getSize())
				.isEqualByComparingTo(Money.zero(USD));
	}

	@Test
	public void shouldNotMatchBuyWhenPriceTooHigh() {
		Position position = createPosition(PositionType.ASK, 100, 100);
		Position order = createPosition(PositionType.BID, 10, 10);

		assertThat(position.match(order)).isEmpty();
	}

	@Test
	public void shouldMatchSellWhenPriceIsHigher() {
		Position position = createPosition(PositionType.BID, 11, 100);
		Position order = createPosition(PositionType.ASK, 10, 10);

		Optional<Trade> trade = position.match(order);

		assertThat(trade)
				.hasValueSatisfying(t -> assertThat(t.getMakerSide()).isEqualTo(PositionType.BID))
				.hasValueSatisfying(t -> assertThat(t.getQuantity())
						.isEqualByComparingTo(USD.amountOf(BigDecimal.TEN)))
				.hasValueSatisfying(t -> assertThat(t.getPrice())
						.isEqualByComparingTo(RATE.valueOf(BigDecimal.valueOf(11))));

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(100)));

		trade.ifPresent(position::execute);

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(90)));
	}

	@Test
	public void shouldMatchSellWhenPriceIsEqual() {
		Position position = createPosition(PositionType.BID, 10, 100);
		Position order = createPosition(PositionType.ASK, 10, 10);

		Optional<Trade> trade = position.match(order);

		assertThat(trade)
				.hasValueSatisfying(t -> assertThat(t.getMakerSide()).isEqualTo(PositionType.BID))
				.hasValueSatisfying(t -> assertThat(t.getQuantity())
						.isEqualByComparingTo(USD.amountOf(BigDecimal.TEN)))
				.hasValueSatisfying(t -> assertThat(t.getPrice())
						.isEqualByComparingTo(RATE.valueOf(BigDecimal.valueOf(10))));

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(100)));

		trade.ifPresent(position::execute);

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(90)));
	}

	@Test
	public void shouldMatchSellWhenPriceIsHigherAndEatWholePosition() {
		Position position = createPosition(PositionType.BID, 11, 10);
		Position order = createPosition(PositionType.ASK, 10, 100);

		Optional<Trade> trade = position.match(order);
		assertThat(trade)
				.hasValueSatisfying(t -> assertThat(t.getMakerSide()).isEqualTo(PositionType.BID))
				.hasValueSatisfying(t -> assertThat(t.getQuantity())
						.isEqualByComparingTo(USD.amountOf(BigDecimal.TEN)))
				.hasValueSatisfying(t -> assertThat(t.getPrice())
						.isEqualByComparingTo(RATE.valueOf(BigDecimal.valueOf(11))));

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.valueOf(10)));

		trade.ifPresent(position::execute);

		assertThat(position.getSize())
				.isEqualByComparingTo(USD.amountOf(BigDecimal.ZERO));
	}

	@Test
	public void shouldNotMatchSellWhenPriceTooLow() {
		assertThat(
				createPosition(PositionType.BID, 1, 100)
						.match(createPosition(PositionType.ASK, 10, 10)))
				.isEmpty();
	}
}