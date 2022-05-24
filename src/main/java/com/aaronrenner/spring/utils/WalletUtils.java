package com.aaronrenner.spring.utils;

import static java.util.Objects.isNull;

import com.aaronrenner.spring.exceptions.BadRequestException;
import com.aaronrenner.spring.models.WalletType;

public class WalletUtils {
	
	private final String badRequestException = "Wallet value %s is not valid";
	
	/*
	 * Helper function that finds an address type.
	 * Warning: This function only can match coins in WalletType
	 */
	public String checkWalletType(String walletAddress) {
		String walletType = null;
		// Determines the wallet type based on internal regex
		for (WalletType w : WalletType.values()) {
           if(w.getRegex() != "") {
        	   if(walletAddress.matches(w.getRegex())) {
        		   walletType = w.toString();
        		   // Only match first
        		   break;
        	   }
           }
        }
		// Ensure a type was found
		if(isNull(walletType))
			throw new BadRequestException(String.format(badRequestException, walletAddress));
		return walletType;
	}

}
