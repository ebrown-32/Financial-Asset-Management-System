package com.iffi.accounts;

import java.util.ArrayList;
import java.util.List;

import com.iffi.assets.Asset;

/**
 * Class that models an "account", with each account having person details and a
 * collection (list) of assets.
 * 
 * @author ebrown
 *
 */
public class Account {

	private final int accountId;
	private String accountNum;
	private String type;
	private Person owner;
	private Person manager;
	private Person beneficiary;
	private List<Asset> assets;

	public Account(String accountNum, String type, Person owner, Person manager, Person beneficiary) {
		super();
		this.accountId = 0;
		this.accountNum = accountNum;
		this.type = type;
		this.owner = owner;
		this.manager = manager;
		this.beneficiary = beneficiary;
		this.assets = new ArrayList<>();
	}

	public Account(int accountId, String accountNum, String type, Person owner, Person manager, Person beneficiary) {
		super();
		this.accountId = accountId;
		this.accountNum = accountNum;
		this.type = type;
		this.owner = owner;
		this.manager = manager;
		this.beneficiary = beneficiary;
		this.assets = new ArrayList<>();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Account: #%s (%s)\n", this.getAccountNum(), this.getType()));
		sb.append(String.format("--------------------\nOwner: %s\n", this.getOwner().toString()));
		sb.append(String.format("--------------------\nManager: %s\n", this.getManager().toString()));
		if (this.getBeneficiary() == null) {
			sb.append(String.format("--------------------\nBeneficiary: %s\n--------------------", "n/a"));
		} else {
			sb.append(String.format("--------------------\nBeneficiary: %s\n--------------------",
					this.getBeneficiary().toString()));
		}
		return sb.toString();
	}

	/**
	 * Add an asset to an account.
	 * 
	 * @param a
	 */
	public void addAsset(Asset a) {
		this.assets.add(a);
	}

	/**
	 * Get the total value of an account. This is done by going through each asset
	 * belonging to an account and summing up the total current value of each asset.
	 * 
	 * @return
	 */
	public double getTotalValue() {
		double result = 0.0;
		for (Asset a : this.assets) {
			result += a.getCurrentValue();
		}
		return result;
	}

	/**
	 * Get the total cost basis or expenses of an account. This is done by going
	 * through each asset belonging to an account and summing up the cost basis of
	 * each asset.
	 * 
	 * @return
	 */
	public double getTotalCost() {
		double result = 0.0;
		for (Asset a : this.assets) {
			result += a.getCostBasis();
		}
		return result;
	}

	/**
	 * Calculates the fee charged on each account. This is done by charging a $100
	 * fee to each Property asset and a $10 fee to each Cryptocurrency.
	 * 
	 * If the account is a "Pro" or "P" account, as opposed to a "Noob" or "N"
	 * account, it receives a 25% discount on the fee total.
	 * 
	 * @return
	 */
	public double getTotalFee() {
		double result = 0.0;
		for (Asset a : this.assets) {
			if (a.getType().compareTo("Property") == 0) {
				result += 100;
			}
			if (a.getType().compareTo("Crypto") == 0) {
				result += 10;
			}
		}
		if (this.type.compareTo("P") == 0) {
			result = result - (result * 0.25);
		}
		return result;
	}

	/**
	 * Calculates the total gain, or return, of all assets on an account. This
	 * method loops through the assets summing up the total gain of each.
	 * 
	 * @return
	 */
	public double getTotalReturn() {
		double result = 0.0;
		for (Asset a : this.assets) {
			result += a.getGain();
		}
		return result;
	}

	/**
	 * Calculates the total gain, or return, of all assets on an account as a
	 * *percentage*. This method utilizes principles found here: <a href=
	 * "https://www.investopedia.com/articles/basics/10/guide-to-calculating-roi.asp">Calculating
	 * Return</a>
	 * 
	 * @return
	 */
	public double getTotalReturnPercentage() {
		return ((this.getTotalValue() - this.getTotalCost()) / this.getTotalCost()) * 100;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public String getType() {
		return type;
	}

	public Person getOwner() {
		return owner;
	}

	public Person getManager() {
		return manager;
	}

	public Person getBeneficiary() {
		return beneficiary;
	}

	public List<Asset> getAssets() {
		return this.assets;
	}

	public int getAccountId() {
		return accountId;
	}
}
