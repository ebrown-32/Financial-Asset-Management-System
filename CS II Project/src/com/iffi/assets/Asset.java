package com.iffi.assets;

/**
 * An abstract class that models a financial asset, with sub-classes of
 * Property, Stock, Crypotocurrency, Put, and Call.
 * 
 * @author ebrown
 *
 */
public abstract class Asset {

	private final int assetId;
	private final String code;
	private final String type;
	private final String label;

	public Asset(String code, String type, String label) {
		super();
		this.assetId = 0;
		this.code = code;
		this.type = type;
		this.label = label;
	}

	public Asset(int assetId, String code, String type, String label) {
		super();
		this.assetId = assetId;
		this.code = code;
		this.type = type;
		this.label = label;
	}

	/**
	 * Returns the current value of an asset according to business rules
	 * individually specified in classes accordingly.
	 * 
	 * @return
	 */
	public abstract double getCurrentValue();

	/**
	 * Returns the cost basis of an asset, that is, how much it cost to acquire the
	 * asset originally.
	 * 
	 * @return
	 */
	public abstract double getCostBasis();

	/**
	 * Calculates the "gain" or "return" of an asset according to financial rules.
	 * Further discovery can be done here. <a href=
	 * "https://www.investopedia.com/articles/basics/10/guide-to-calculating-roi.asp">Calculating
	 * Return</a>
	 * 
	 * @return
	 */
	public double getGain() {
		return getCurrentValue() - getCostBasis();
	}

	/**
	 * Calculates the *percentage* "gain" or "return" of an asset according to
	 * financial rules. Further discovery can be done here. <a href=
	 * "https://www.investopedia.com/articles/basics/10/guide-to-calculating-roi.asp">Calculating
	 * Return</a>
	 * 
	 * @return
	 */
	public double getPercentageGain() {
		return (getGain() / getCostBasis()) * 100;
	}

	public String getCode() {
		return code;
	}

	public String getType() {
		return type;
	}

	public String getLabel() {
		return label;
	}

	public int getAssetId() {
		return assetId;
	}
}
