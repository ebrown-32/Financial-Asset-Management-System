package com.iffi.assets;

import java.time.LocalDate;

/**
 * Models a Cryptocurrency financial asset.
 * 
 * @author ebrown
 *
 */
public class Cryptocurrency extends Asset {

	private double exchangeRate;
	private double exchangeFee;
	private LocalDate purchaseDate;
	private Double purchaseExchangeRate;
	private Double numberOfCoins;

	public Cryptocurrency(String code, String type, String label, double exchangeRate, double exchangeFee) {
		super(code, type, label);
		this.exchangeRate = exchangeRate;
		this.exchangeFee = exchangeFee;
	}

	public Cryptocurrency(Cryptocurrency c, LocalDate purchaseDate, Double purchaseExchangeRate, Double numberOfCoins) {
		this(c.getCode(), c.getType(), c.getLabel(), c.getExchangeRate(), c.getExchangeFee(), purchaseDate,
				purchaseExchangeRate, numberOfCoins);
	}

	public Cryptocurrency(String code, String type, String label, double exchangeRate, double exchangeFee,
			LocalDate purchaseDate, Double purchaseExchangeRate, Double numberOfCoins) {
		super(code, type, label);
		this.exchangeRate = exchangeRate;
		this.exchangeFee = exchangeFee;
		this.purchaseDate = purchaseDate;
		this.purchaseExchangeRate = purchaseExchangeRate;
		this.numberOfCoins = numberOfCoins;
	}

	public Cryptocurrency(int assetId, String code, String type, String label, double exchangeRate, double exchangeFee,
			LocalDate purchaseDate, Double purchaseExchangeRate, Double numberOfCoins) {
		super(assetId, code, type, label);
		this.exchangeRate = exchangeRate;
		this.exchangeFee = exchangeFee;
		this.purchaseDate = purchaseDate;
		this.purchaseExchangeRate = purchaseExchangeRate;
		this.numberOfCoins = numberOfCoins;
	}

	@Override
	public double getCurrentValue() {
		return (numberOfCoins * exchangeRate) * (1 - (exchangeFee / 100));
	}

	@Override
	public double getCostBasis() {
		return numberOfCoins * purchaseExchangeRate;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s\t%s (%s)\n", this.getCode(), this.getLabel(), this.getType()));
		sb.append(String.format("  Cost Basis: %.3f coins @ $%.2f on %s\n", this.getNumOfCoins(),
				this.getPurchaseExchangeRate(), this.getPurchaseDate()));
		sb.append(String.format("  Value Basis: %.3f coins @ $%.2f less %.2f%% \t %.2f%% \t $%.2f",
				this.getNumOfCoins(), this.getExchangeRate(), this.getExchangeFee(), this.getPercentageGain(),
				this.getCurrentValue()));
		return sb.toString();
	}

	public double getNumOfCoins() {
		return numberOfCoins;
	}

	public double getPurchaseExchangeRate() {
		return purchaseExchangeRate;
	}

	public double getExchangeFee() {
		return exchangeFee;
	}

	public double getExchangeRate() {
		return exchangeRate;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}
}
