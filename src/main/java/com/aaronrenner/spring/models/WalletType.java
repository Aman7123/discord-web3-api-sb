package com.aaronrenner.spring.models;

public enum WalletType {
    ETHERIUM ("eth", "Ξ", "0x[a-fA-F0-9]{40}"),
    BITCOIN  ("btc", "₿", "[13][a-km-zA-HJ-NP-Z1-9]{25,34}"),
    SOLANA   ("sol", "◎", "[1-9A-HJ-NP-Za-km-z]{32,44}"),
    DOGECOIN ("doge", "Ð", "D{1}[5-9A-HJ-NP-U]{1}[1-9A-HJ-NP-Za-km-z]{32}");

    private String ticker;
    private String symbol;
    private String regex;

    WalletType(String ticker, String symbol, String regex) {
        this.ticker = ticker;
        this.symbol = symbol;
        this.regex  = regex;
    }

    public String getSymbol() {
        return this.symbol;
    }
    
    public String getRegex() {
    	return this.regex;
    }

    @Override
    public String toString() {
        return this.ticker;
    }

    public static WalletType fromString(String ticker) {
        if (ticker != null) {
            for (WalletType unit : WalletType.values()) {
                if (ticker.equalsIgnoreCase(unit.ticker)) {
                    return unit;
                }
            }
        }
        return WalletType.valueOf(ticker);
    }
}
