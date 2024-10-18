package com.tgac.exchange.domain.wallet;

import java.util.Optional;

public interface WalletRepository {
	Optional<Wallet> findById(WalletId id);

	void save(Wallet wallet);
}
