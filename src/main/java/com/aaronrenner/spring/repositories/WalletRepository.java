package com.aaronrenner.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.aaronrenner.spring.models.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	
	Boolean existsByAddressAllIgnoreCase(String walletAddress);
	Integer deleteByAddressAllIgnoreCase(String walletAddress);
	
}
