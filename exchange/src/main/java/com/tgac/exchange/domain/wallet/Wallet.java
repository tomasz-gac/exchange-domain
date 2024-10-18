package com.tgac.exchange.domain.wallet;

import com.tgac.exchange.domain.Currency;
import com.tgac.exchange.domain.Money;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jmolecules.ddd.types.AggregateRoot;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Wallet implements AggregateRoot<Wallet, WalletId> {
	private final WalletId id;
	private final Map<Currency, Balance> balances = new HashMap<>();

	public void transferTo(Wallet target, Money amount) throws InsufficientFundsException {
		withdraw(amount);
		target.add(amount);
	}

	public void add(Money money) {
		Balance balance = Optional.ofNullable(balances.get(money.getUnit()))
				.orElseGet(() -> new Balance(new BalanceId(), Money.zero(money.getUnit())));
		balance.add(money);
		balances.put(money.getUnit(), balance);
	}

	public void withdraw(Money money) throws InsufficientFundsException {
		Balance balance = Optional.ofNullable(balances.get(money.getUnit()))
				.orElseGet(() -> new Balance(new BalanceId(), Money.zero(money.getUnit())));
		balance.withdraw(money);
		balances.put(money.getUnit(), balance);
	}

}
