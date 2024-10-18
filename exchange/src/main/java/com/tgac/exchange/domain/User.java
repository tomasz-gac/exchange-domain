package com.tgac.exchange.domain;

import com.tgac.exchange.domain.market.Trade;
import com.tgac.exchange.domain.market.TradeId;
import com.tgac.exchange.domain.order.Order;
import com.tgac.exchange.domain.order.OrderId;
import com.tgac.exchange.domain.wallet.Wallet;
import com.tgac.exchange.domain.wallet.WalletId;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class User implements AggregateRoot<User, UserId> {
	private final UserId id;
	private final Association<Wallet, WalletId> accountWallet;
	private final Association<Wallet, WalletId> marketWallet;
	private final List<Association<Order, OrderId>> orders;
	private final List<Association<Trade, TradeId>> trades;
}
