package com.iffi.assets;

import java.time.LocalDate;

/**
 * Models a property financial asset.
 * 
 * @author ebrown
 *
 */
public class Property extends Asset {

	private final double appraisedValue;
	private Double purchasePrice;
	private LocalDate purchaseDate;

	public Property(String code, String type, String label, double appraisedValue) {
		super(code, type, label);
		this.appraisedValue = appraisedValue;
	}

	public Property(Property p, LocalDate purchaseDate, Double purchasePrice) {
		this(p.getCode(), p.getType(), p.getLabel(), p.getAppraisedValue(), purchaseDate, purchasePrice);
	}

	public Property(String code, String type, String label, double appraisedValue, LocalDate purchaseDate,
			Double purchasePrice) {
		super(code, type, label);
		this.appraisedValue = appraisedValue;
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
	}

	public Property(int assetId, String code, String type, String label, double appraisedValue, LocalDate purchaseDate,
			Double purchasePrice) {
		super(assetId, code, type, label);
		this.appraisedValue = appraisedValue;
		this.purchaseDate = purchaseDate;
		this.purchasePrice = purchasePrice;
	}

	@Override
	public double getCurrentValue() {
		return getAppraisedValue();
	}

	@Override
	public double getCostBasis() {
		return getPurchasePrice();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s\t%s (%s)\n", this.getCode(), this.getLabel(), this.getType()));
		sb.append(String.format("  Cost Basis: purchased @ %.2f on %s\n", this.getCostBasis(), this.getPurchaseDate()));
		sb.append(String.format("  Value Basis: appraised @ %.2f \t %.2f%% \t $%.2f", this.getCurrentValue(),
				this.getPercentageGain(), this.getCurrentValue()));
		return sb.toString();
	}

	public double getAppraisedValue() {
		return this.appraisedValue;
	}

	public double getPurchasePrice() {
		return this.purchasePrice;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

}
