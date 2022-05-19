package com.iffi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import com.iffi.accounts.Account;
import com.iffi.assets.Asset;
import com.iffi.assets.Call;
import com.iffi.assets.Cryptocurrency;
import com.iffi.assets.Property;
import com.iffi.assets.Put;
import com.iffi.assets.Stock;

/**
 * 
 * Loads a list of assets from a database, assigning them appropriately
 * to each account in the given list of accounts.
 * 
 * @author ebrown
 *
 */
public class AssetLoader {

	public static final Logger LOG = LogManager.getLogger(AssetLoader.class);

	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}

	/**
	 * Loads the assets into correct accounts
	 */
	public static void loadAllAssets(List<Account> accounts) {
		Asset a = null;
		Connection conn = DatabaseUtils.establishConnection();
		String query = "select * from AccountAsset\r\n" + "left join Asset a on a.assetId = AccountAsset.assetId\r\n"
				+ "where AccountAsset.accountId = ?;";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			for (Account acct : accounts) {
				ps.setInt(1, acct.getAccountId());
				rs = ps.executeQuery();
				// instantiate new assets depending on the type
				while (rs.next()) {
					int assetId = rs.getInt("assetId");
					String assetCode = rs.getString("assetCode");
					String assetType = rs.getString("assetType");
					String assetLabel = rs.getString("assetLabel");
					String optionType = rs.getString("optionType");
					if (assetType.compareTo("Property") == 0) {
						double appraisedValue = rs.getDouble("appraisedValue");
						LocalDate purchaseDate = rs.getObject("purchaseDate", LocalDate.class);
						double purchasePrice = rs.getDouble("purchasePrice");
						a = new Property(assetId, assetCode, assetType, assetLabel, appraisedValue, purchaseDate,
								purchasePrice);
						acct.addAsset(a);
					}
					if (assetType.compareTo("Crypto") == 0) {
						double exchangeRate = rs.getDouble("exchangeRate");
						double exchangeFee = rs.getDouble("exchangeFee");
						LocalDate purchaseDate = rs.getObject("purchaseDate", LocalDate.class);
						double purchaseExchangeRate = rs.getDouble("purchaseExchangeRate");
						double numberOfCoins = rs.getDouble("numberOfCoins");
						a = new Cryptocurrency(assetId, assetCode, assetType, assetLabel, exchangeRate, exchangeFee,
								purchaseDate, purchaseExchangeRate, numberOfCoins);
						acct.addAsset(a);
					}
					if (assetType.compareTo("Stock") == 0) {
						if (optionType == null) {
							String symbol = rs.getString("symbol");
							double sharePrice = rs.getDouble("sharePrice");
							LocalDate purchaseDate = rs.getObject("purchaseDate", LocalDate.class);
							double purchaseSharePrice = rs.getDouble("purchaseSharePrice");
							double numberOfShares = rs.getDouble("numberOfShares");
							double dividendTotal = rs.getDouble("dividendTotal");
							a = new Stock(assetId, assetCode, assetType, assetLabel, symbol, sharePrice, purchaseDate,
									purchaseSharePrice, numberOfShares, dividendTotal);
							acct.addAsset(a);
						} else {
							if (optionType.compareTo("P") == 0) {
								String symbol = rs.getString("symbol");
								double sharePrice = rs.getDouble("sharePrice");
								double shareLimit = rs.getDouble("shareLimit");
								double strikePrice = rs.getDouble("strikePrice");
								double premium = rs.getDouble("premium");
								LocalDate purchaseDate = rs.getObject("purchaseDate", LocalDate.class);
								LocalDate strikeDate = rs.getObject("strikeDate", LocalDate.class);
								a = new Put(assetId, assetCode, "Put", assetLabel, symbol, sharePrice, shareLimit,
										strikePrice, premium, purchaseDate, strikeDate);
								acct.addAsset(a);
							} else if (optionType.compareTo("C") == 0) {
								String symbol = rs.getString("symbol");
								double sharePrice = rs.getDouble("sharePrice");
								double shareLimit = rs.getDouble("shareLimit");
								double strikePrice = rs.getDouble("strikePrice");
								double premium = rs.getDouble("premium");
								LocalDate purchaseDate = rs.getObject("purchaseDate", LocalDate.class);
								LocalDate strikeDate = rs.getObject("strikeDate", LocalDate.class);
								a = new Call(assetId, assetCode, "Call", assetLabel, symbol, sharePrice, shareLimit,
										strikePrice, premium, purchaseDate, strikeDate);
								acct.addAsset(a);
							}
						}
					}

				}
			}
		} catch (SQLException e) {
			LOG.error("Could not execute query", e);
			throw new RuntimeException(e);
		}
		try {
			if (!(rs == null)) {
				rs.close();
			}
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}
}
