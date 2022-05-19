package com.iffi.assets;

import java.time.LocalDate;

/**
 * Class that models a Stock, which is a financial asset.
 * 
 * @author ebrown
 *
 */
public class Stock extends Asset {

	private final String symbol;
	private final double currentSharePrice;
	private LocalDate purchaseDate;
	private Double purchaseSharePrice;
	private Double numberOfShares;
	private Double dividendTotal;

	public Stock(String code, String type, String label, String symbol, double sharePrice) {
		super(code, type, label);
		this.symbol = symbol;
		this.currentSharePrice = sharePrice;
	}

	public Stock(Stock s, LocalDate purchaseDate, Double purchaseSharePrice, Double numberOfShares,
			Double dividendTotal) {
		this(s.getCode(), s.getType(), s.getLabel(), s.getSymbol(), s.getCurrentSharePrice(), purchaseDate,
				purchaseSharePrice, numberOfShares, dividendTotal);
	}

	public Stock(String code, String type, String label, String symbol, Double sharePrice, LocalDate purchaseDate,
			Double purchaseSharePrice, Double numberOfShares, Double dividendTotal) {
		super(code, type, label);
		this.symbol = symbol;
		this.currentSharePrice = sharePrice;
		this.purchaseDate = purchaseDate;
		this.purchaseSharePrice = purchaseSharePrice;
		this.numberOfShares = numberOfShares;
		this.dividendTotal = dividendTotal;
	}

	public Stock(int assetId, String code, String type, String label, String symbol, Double sharePrice,
			LocalDate purchaseDate, Double purchaseSharePrice, Double numberOfShares, Double dividendTotal) {
		super(assetId, code, type, label);
		this.symbol = symbol;
		this.currentSharePrice = sharePrice;
		this.purchaseDate = purchaseDate;
		this.purchaseSharePrice = purchaseSharePrice;
		this.numberOfShares = numberOfShares;
		this.dividendTotal = dividendTotal;
	}

	@Override
	public double getCurrentValue() {
		return (getCurrentSharePrice() * getNumOfShares()) + getDividendTotal();
	}

	@Override
	public double getCostBasis() {
		return getPurchaseSharePrice() * getNumOfShares();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s\t%s (%s)\n", this.getCode(), this.getLabel(), this.getType()));
		sb.append(String.format("  Cost Basis: %.2f @ %.2f on %s\n", this.getNumOfShares(),
				this.getPurchaseSharePrice(), this.getPurchaseDate()));
		sb.append(String.format("  Value Basis: %.2f @ %.2f \t %.2f%% \t $%.2f", this.getNumOfShares(),
				this.getCurrentSharePrice(), this.getPercentageGain(), this.getCurrentValue()));
		return sb.toString();
	}

	public String getSymbol() {
		return symbol;
	}

	public double getNumOfShares() {
		return numberOfShares;
	}

	public double getPurchaseSharePrice() {
		return purchaseSharePrice;
	}

	public double getCurrentSharePrice() {
		return currentSharePrice;
	}

	public double getDividendTotal() {
		return dividendTotal;
	}

	public String getPurchaseDate() {
		return purchaseDate.toString();
	}
}
