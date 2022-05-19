package com.iffi.assets;

import java.time.LocalDate;

/**
 * Models a Put financial asset.
 * <a href="https://www.investopedia.com/terms/p/put.asp">What is a Put?</a>
 * 
 * @author ebrown
 *
 */
public class Put extends Asset {

	private final String symbol;
	private final Double shareLimit;
	private final Double sharePrice;
	private final Double strikePrice;
	private final double premium;
	private final LocalDate purchaseDate;
	private final LocalDate strikeDate;

	public Put(Stock s, LocalDate purchaseDate, Double strikePrice, Double shareLimit, double premium,
			LocalDate strikeDate) {
		this(s.getCode(), "P", s.getLabel(), s.getSymbol(), s.getCurrentSharePrice(), shareLimit, strikePrice,
				premium, purchaseDate, strikeDate);
	}

	public Put(String code, String type, String label, String symbol, Double sharePrice, Double shareLimit,
			Double strikePrice, double premium, LocalDate purchaseDate, LocalDate strikeDate) {
		super(code, type, label);
		this.symbol = symbol;
		this.shareLimit = shareLimit;
		this.sharePrice = sharePrice;
		this.strikePrice = strikePrice;
		this.premium = premium;
		this.purchaseDate = purchaseDate;
		this.strikeDate = strikeDate;
	}

	public Put(int assetId, String code, String type, String label, String symbol, Double sharePrice, Double shareLimit,
			Double strikePrice, double premium, LocalDate purchaseDate, LocalDate strikeDate) {
		super(assetId, code, type, label);
		this.symbol = symbol;
		this.shareLimit = shareLimit;
		this.sharePrice = sharePrice;
		this.strikePrice = strikePrice;
		this.premium = premium;
		this.purchaseDate = purchaseDate;
		this.strikeDate = strikeDate;
	}

	@Override
	public double getCostBasis() {
		// always zero because sellers
		return 0;
	}

	@Override
	public double getCurrentValue() {
		double result = 0;
		if (getSharePrice() > getStrikePrice()) {
			result = (getStrikePrice() - getSharePrice() + getPremium()) * getShareLimit();
		} else if (getSharePrice() <= getStrikePrice()) {
			result = getPremium() * getShareLimit();
		}
		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s\t%s (%s)\n", this.getCode(), this.getLabel(), this.getType()));
		sb.append(String.format("  Sell upto %.2f shares @ $%.2f til %s\n", this.getShareLimit(), this.getStrikePrice(),
				this.getStrikeDate()));
		sb.append(String.format("  Premium of $%.2f/share (%.2f total)\n", this.getPremium(), this.getCurrentValue()));
		sb.append(String.format("  Share Price: $%.2f\n", this.getSharePrice()));
		sb.append(String.format("  Put Value: %.2f shares @ $%.2f = $%.2f", this.getShareLimit(), this.getPremium(),
				this.getCurrentValue()));
		return sb.toString();
	}

	public String getSymbol() {
		return symbol;
	}

	public Double getShareLimit() {
		return shareLimit;
	}

	public Double getSharePrice() {
		return sharePrice;
	}

	public Double getStrikePrice() {
		return strikePrice;
	}

	public double getPremium() {
		return premium;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public LocalDate getStrikeDate() {
		return strikeDate;
	}
}
