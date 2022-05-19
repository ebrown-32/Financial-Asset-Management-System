package com.iffi.assets;

import java.time.LocalDate;

/**
 * Models a Call financial asset.
 * <a href="https://www.investopedia.com/terms/c/call.asp">What is a Call?</a>
 * 
 * @author ebrown
 *
 */
public class Call extends Asset {

	private final String symbol;
	private final Double shareLimit;
	private final Double sharePrice;
	private final Double strikePrice;
	private final double premium;
	private final LocalDate purchaseDate;
	private final LocalDate strikeDate;

	public Call(Stock s, LocalDate purchaseDate, Double strikePrice, Double shareLimit, double premium,
			LocalDate strikeDate) {
		this(s.getCode(), "C", s.getLabel(), s.getSymbol(), s.getCurrentSharePrice(), shareLimit, strikePrice,
				premium, purchaseDate, strikeDate);
	}

	public Call(String code, String type, String label, String symbol, Double sharePrice, Double shareLimit,
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

	public Call(int assetId, String code, String type, String label, String symbol, Double sharePrice,
			Double shareLimit, Double strikePrice, double premium, LocalDate purchaseDate, LocalDate strikeDate) {
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
		return getPremium() * getShareLimit();
	}

	@Override
	public double getCurrentValue() {
		double result = 0;
		if (getSharePrice() > getStrikePrice()) {
			result = (getSharePrice() - getStrikePrice()) * getShareLimit();
		} else if (getSharePrice() <= getStrikePrice()) {
			result = 0;
		}
		return result;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s\t%s (%s)\n", this.getCode(), this.getLabel(), this.getType()));
		sb.append(String.format("  Buy upto %.2f shares @ $%.2f til %s\n", this.getShareLimit(), this.getStrikePrice(),
				this.getStrikeDate()));
		sb.append(String.format("  Premium of $%.2f/share (%.2f total)\n", this.getPremium(), this.getCurrentValue()));
		sb.append(String.format("  Share Price: $%.2f\n", this.getSharePrice()));
		sb.append(String.format("  Call Value: %.2f shares @ $%.2f = $%.2f", this.getShareLimit(), this.getPremium(),
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
