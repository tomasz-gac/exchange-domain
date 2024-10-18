package com.tgac.exchange.domain.wallet;

import com.tgac.exchange.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jmolecules.ddd.types.Entity;

@Getter
@AllArgsConstructor
public class Balance implements Entity<Wallet, BalanceId> {
	private final BalanceId id;
	private Money money;

	public void add(Money moneyToAdd) {
		money = money.add(moneyToAdd);
	}

	public void withdraw(Money moneyToRemove) throws InsufficientFundsException {
		Money newMoney = money.subtract(moneyToRemove);
		if (newMoney.getValue().signum() < 0) {
			throw new InsufficientFundsException(id);
		}
		money = newMoney;
	}
}
