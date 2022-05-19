package com.iffi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import com.iffi.database.DatabaseUtils;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class AccountData {

	public static final Logger LOG = LogManager.getLogger(AccountData.class);

	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}
	
	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		Connection conn = DatabaseUtils.establishConnection();
		try {

			String query = "SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `State` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `State` (\r\n" + "  `stateId` INT NULL AUTO_INCREMENT,\r\n"
					+ "  `state` VARCHAR(45) NULL,\r\n" + "  PRIMARY KEY (`stateId`))\r\n" + "ENGINE = InnoDB;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `Country` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `Country` (\r\n" + "  `countryId` INT NULL AUTO_INCREMENT,\r\n"
					+ "  `country` VARCHAR(45) NULL,\r\n" + "  PRIMARY KEY (`countryId`))\r\n" + "ENGINE = InnoDB;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `Address` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `Address` (\r\n" + "  `addressId` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `street` VARCHAR(45) NULL DEFAULT NULL,\r\n" + "  `city` VARCHAR(45) NULL DEFAULT NULL,\r\n"
					+ "  `zip` VARCHAR(45) NULL DEFAULT NULL,\r\n" + "  `stateId` INT NULL,\r\n"
					+ "  `countryId` INT NULL,\r\n" + "  PRIMARY KEY (`addressId`),\r\n"
					+ "  INDEX `stateId_idx` (`stateId` ASC) VISIBLE,\r\n"
					+ "  INDEX `countryId_idx` (`countryId` ASC) VISIBLE,\r\n" + "  CONSTRAINT `stateId`\r\n"
					+ "    FOREIGN KEY (`stateId`)\r\n" + "    REFERENCES `State` (`stateId`)\r\n"
					+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `countryId`\r\n"
					+ "    FOREIGN KEY (`countryId`)\r\n" + "    REFERENCES `Country` (`countryId`)\r\n"
					+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `Person` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `Person` (\r\n" + "  `personId` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `personCode` VARCHAR(45) NOT NULL,\r\n" + "  `lastName` VARCHAR(45) NULL DEFAULT NULL,\r\n"
					+ "  `firstName` VARCHAR(45) NULL DEFAULT NULL,\r\n" + "  `addressId` INT NOT NULL,\r\n"
					+ "  PRIMARY KEY (`personId`),\r\n" + "  INDEX `addressId_idx` (`addressId` ASC) VISIBLE,\r\n"
					+ "  UNIQUE INDEX `personCode_UNIQUE` (`personCode` ASC) VISIBLE,\r\n"
					+ "  CONSTRAINT `addressId`\r\n" + "    FOREIGN KEY (`addressId`)\r\n"
					+ "    REFERENCES `Address` (`addressId`)\r\n" + "    ON DELETE CASCADE\r\n"
					+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `Account` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `Account` (\r\n"
					+ "  `accountId` INT NOT NULL AUTO_INCREMENT COMMENT '\\\\n',\r\n"
					+ "  `accountNumber` VARCHAR(45) NOT NULL,\r\n" + "  `type` VARCHAR(45) NOT NULL,\r\n"
					+ "  `ownerId` INT NOT NULL,\r\n" + "  `managerId` INT NOT NULL,\r\n"
					+ "  `beneficiaryId` INT NULL,\r\n" + "  PRIMARY KEY (`accountId`),\r\n"
					+ "  UNIQUE INDEX `accountNumber_UNIQUE` (`accountNumber` ASC) VISIBLE,\r\n"
					+ "  INDEX `ownerId_idx` (`ownerId` ASC) VISIBLE,\r\n"
					+ "  INDEX `managerId_idx` (`managerId` ASC) VISIBLE,\r\n"
					+ "  INDEX `beneficiaryId_idx` (`beneficiaryId` ASC) VISIBLE,\r\n"
					+ "  CONSTRAINT `beneficiaryId`\r\n" + "    FOREIGN KEY (`beneficiaryId`)\r\n"
					+ "    REFERENCES `Person` (`personId`)\r\n" + "    ON DELETE NO ACTION\r\n"
					+ "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `managerId`\r\n"
					+ "    FOREIGN KEY (`managerId`)\r\n" + "    REFERENCES `Person` (`personId`)\r\n"
					+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION,\r\n" + "  CONSTRAINT `ownerId`\r\n"
					+ "    FOREIGN KEY (`ownerId`)\r\n" + "    REFERENCES `Person` (`personId`)\r\n"
					+ "    ON DELETE NO ACTION\r\n" + "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `Asset` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `Asset` (\r\n" + "  `assetId` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `assetCode` VARCHAR(45) NOT NULL,\r\n" + "  `assetType` VARCHAR(45) NOT NULL,\r\n"
					+ "  `assetLabel` VARCHAR(45) NOT NULL,\r\n" + "  `appraisedValue` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `exchangeRate` DOUBLE NULL DEFAULT NULL,\r\n" + "  `exchangeFee` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `symbol` VARCHAR(45) NULL DEFAULT NULL,\r\n" + "  `sharePrice` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  PRIMARY KEY (`assetId`),\r\n"
					+ "  UNIQUE INDEX `assetCode_UNIQUE` (`assetCode` ASC) VISIBLE)\r\n" + "ENGINE = InnoDB\r\n"
					+ "DEFAULT CHARACTER SET = utf8mb4;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `Email` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `Email` (\r\n" + "  `emailId` INT NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `emailAddress` VARCHAR(45) NULL DEFAULT NULL,\r\n" + "  `personId` INT NULL DEFAULT NULL,\r\n"
					+ "  PRIMARY KEY (`emailId`),\r\n" + "  INDEX `personId_idx` (`personId` ASC) VISIBLE,\r\n"
					+ "  CONSTRAINT `personId`\r\n" + "    FOREIGN KEY (`personId`)\r\n"
					+ "    REFERENCES `Person` (`personId`)\r\n" + "    ON DELETE NO ACTION\r\n"
					+ "    ON UPDATE NO ACTION)\r\n" + "ENGINE = InnoDB;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "DROP TABLE IF EXISTS `AccountAsset` ;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();

			query = "CREATE TABLE IF NOT EXISTS `AccountAsset` (\r\n"
					+ "  `accountAssetId` INT(11) NOT NULL AUTO_INCREMENT,\r\n"
					+ "  `accountId` INT(11) NOT NULL,\r\n"
					+ "  `assetId` INT(11) NOT NULL,\r\n"
					+ "  `purchaseDate` VARCHAR(45) NULL DEFAULT NULL,\r\n"
					+ "  `purchasePrice` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `purchaseExchangeRate` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `numberOfCoins` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `numberOfShares` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `purchaseSharePrice` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `dividendTotal` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `strikeDate` VARCHAR(45) NULL DEFAULT NULL,\r\n"
					+ "  `shareLimit` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `premium` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `strikePrice` DOUBLE NULL DEFAULT NULL,\r\n"
					+ "  `optionType` VARCHAR(45) NULL DEFAULT NULL,\r\n"
					+ "  PRIMARY KEY (`accountAssetId`),\r\n"
					+ "  INDEX `accountId_idx` (`accountId` ASC) VISIBLE,\r\n"
					+ "  INDEX `assetId_idx` (`assetId` ASC) VISIBLE,\r\n"
					+ "  CONSTRAINT `accountId`\r\n"
					+ "    FOREIGN KEY (`accountId`)\r\n"
					+ "    REFERENCES `Account` (`accountId`)\r\n"
					+ "    ON DELETE NO ACTION\r\n"
					+ "    ON UPDATE NO ACTION,\r\n"
					+ "  CONSTRAINT `assetId`\r\n"
					+ "    FOREIGN KEY (`assetId`)\r\n"
					+ "    REFERENCES `Asset` (`assetId`)\r\n"
					+ "    ON DELETE NO ACTION\r\n"
					+ "    ON UPDATE NO ACTION)\r\n"
					+ "ENGINE = InnoDB\r\n"
					+ "DEFAULT CHARACTER SET = utf8mb4;";
			ps = conn.prepareStatement(query);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not clear database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city,
			String state, String zip, String country) {
		Connection conn = DatabaseUtils.establishConnection();
		try {
			// insert country into the Country table, and grab the auto incremented
			// countryId key it made
			String query = "insert into Country (country) values (?);";
			PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, country);
			ps.executeUpdate();
			ResultSet countryKeys = ps.getGeneratedKeys();
			countryKeys.next();
			int countryKey = countryKeys.getInt(1);
			countryKeys.close();
			ps.close();
			// insert state into the State table, and grab the auto incremented stateId key
			// it made
			query = "insert into State (state) values (?);";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, state);
			ps.executeUpdate();
			ResultSet stateKeys = ps.getGeneratedKeys();
			stateKeys.next();
			int stateKey = stateKeys.getInt(1);
			stateKeys.close();
			ps.close();
			// insert fields into Address table, including the state and country keys we
			// grabbed previously
			// also grab the auto-incremented addressId key to use for the person insertion
			// below
			query = "insert into Address (street, city, zip, stateId, countryId) values (?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, street);
			ps.setString(2, city);
			ps.setString(3, zip);
			ps.setInt(4, stateKey);
			ps.setInt(5, countryKey);
			ps.executeUpdate();
			ResultSet addressKeys = ps.getGeneratedKeys();
			addressKeys.next();
			int addressKey = addressKeys.getInt(1);
			addressKeys.close();
			ps.close();
			// insert fields into Person table, using the addressId we grabbed above
			query = "insert into Person (personCode, lastName, firstName, addressId) values (?, ?, ?, ?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);
			ps.setString(2, lastName);
			ps.setString(3, firstName);
			ps.setInt(4, addressKey);
			ps.executeUpdate();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("failed to insert a person record", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Given a person code, returns the primary key of that person, retrieved from the database
	 * @param personCode
	 * @return
	 */
	public static int getPersonId(String personCode) {
		Connection conn = DatabaseUtils.establishConnection();
		String query = "select personId from Person where personCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int personId = 0;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, personCode);

			rs = ps.executeQuery();

			while (rs.next()) {
				personId = rs.getInt("personId");
			}
		} catch (SQLException e1) {
			LOG.error("Could not retreive personId", e1);
			throw new RuntimeException(e1);
		}
		try {
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
		return personId;
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 *
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		// get the person id given the person code
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		int personId = getPersonId(personCode);
		try {
			// add the given email to that person id
			String query = "insert into Email (emailAddress, personId) values (?, ?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			ps.setInt(2, personId);
			ps.executeUpdate();
		} catch (SQLException e1) {
			LOG.error("Could not insert email record", e1);
			throw new RuntimeException(e1);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a crypto currency asset record to the database with the provided data.
	 *
	 * @param assetCode
	 * @param label
	 * @param exchangeRate
	 * @param exchangeFeeRate
	 */
	public static void addCrypto(String assetCode, String label, double exchangeRate, double exchangeFeeRate) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		try {
			String query = "insert into Asset (assetCode, assetType, assetLabel, exchangeRate, exchangeFee) "
					+ "values (?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(query);

			ps.setString(1, assetCode);
			ps.setString(2, "Crypto");
			ps.setString(3, label);
			ps.setDouble(4, exchangeRate);
			ps.setDouble(5, exchangeFeeRate);
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not insert crypto record", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a property asset record to the database with the provided data.
	 *
	 * @param assetCode
	 * @param label
	 * @param appraisedValue
	 */
	public static void addProperty(String assetCode, String label, double appraisedValue) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		try {
			String query = "insert into Asset (assetCode, assetType, assetLabel, appraisedValue) "
					+ "values (?, ?, ?, ?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "Property");
			ps.setString(3, label);
			ps.setDouble(4, appraisedValue);
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not insert property record", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a stock asset record to the database with the provided data.
	 *
	 * @param assetCode
	 * @param label
	 * @param stockSymbol
	 * @param sharePrice
	 */
	public static void addStock(String assetCode, String label, String stockSymbol, Double sharePrice) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		try {
			String query = "insert into Asset (assetCode, assetType, assetLabel, symbol, sharePrice) "
					+ "values (?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);
			ps.setString(2, "Stock");
			ps.setString(3, label);
			ps.setString(4, stockSymbol);
			ps.setDouble(5, sharePrice);
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not add stock record", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds an account record to the database with the given data. If the account
	 * has no beneficiary, the <code>beneficiaryCode</code> will be
	 * <code>null</code>. The <code>accountType</code> is expected to be either
	 * <code>"N"</code> (noob) or <code>"P"</code> (pro).
	 *
	 * @param accountNumber
	 * @param accountType
	 * @param ownerCode
	 * @param managerCode
	 * @param beneficiaryCode
	 */
	public static void addAccount(String accountNumber, String accountType, String ownerCode, String managerCode,
			String beneficiaryCode) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		String query;
		try {
			if (beneficiaryCode != null) {
				query = "insert into Account (accountNumber, type, ownerId, managerId, beneficiaryId) "
						+ "values (?, ?, ?, ?, ?);";
				ps = conn.prepareStatement(query);
				ps.setString(1, accountNumber);
				ps.setString(2, accountType);
				ps.setInt(3, getPersonId(ownerCode));
				ps.setInt(4, getPersonId(managerCode));
				ps.setInt(5, getPersonId(beneficiaryCode));
			} else {
				query = "insert into Account (accountNumber, type, ownerId, managerId) " + "values (?, ?, ?, ?);";
				ps = conn.prepareStatement(query);
				ps.setString(1, accountNumber);
				ps.setString(2, accountType);
				ps.setInt(3, getPersonId(ownerCode));
				ps.setInt(4, getPersonId(managerCode));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not add an account record", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Given an account number, returns the primary key of an account from the database
	 * @param accountNumber
	 * @return
	 */
	public static int getAccountId(String accountNumber) {
		Connection conn = DatabaseUtils.establishConnection();
		String query = "select accountId from Account where accountNumber = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int accountId = 0;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, accountNumber);

			rs = ps.executeQuery();

			while (rs.next()) {
				accountId = rs.getInt("accountId");
			}
		} catch (SQLException e1) {
			LOG.error("Could not retreive accountId", e1);
			throw new RuntimeException(e1);
		}
		try {
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
		return accountId;
	}

	/**
	 * Given an asset code, returns the primary key of the corresponding asset from the database
	 * @param assetCode
	 * @return
	 */
	public static int getAssetId(String assetCode) {
		Connection conn = DatabaseUtils.establishConnection();
		String query = "select assetId from Asset where assetCode = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int assetId = 0;
		try {
			ps = conn.prepareStatement(query);
			ps.setString(1, assetCode);

			rs = ps.executeQuery();

			while (rs.next()) {
				assetId = rs.getInt("assetId");
			}
		} catch (SQLException e1) {
			LOG.error("Could not get assetId", e1);
			throw new RuntimeException(e1);
		}
		try {
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
		return assetId;
	}

	/**
	 * Adds a crypto currency asset corresponding to <code>assetCode</code> (which
	 * is assumed to already exist in the database) to the account corresponding to
	 * the provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchaseExchangeRate
	 * @param numberOfCoins
	 */
	public static void addCryptoToAccount(String accountNumber, String assetCode, String purchaseDate,
			double purchaseExchangeRate, double numberOfCoins) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		int accountKey = getAccountId(accountNumber);
		int assetKey = getAssetId(assetCode);
		try {
			String query = "insert into AccountAsset (accountId, assetId, purchaseDate, purchaseExchangeRate, numberOfCoins) values"
					+ "(?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, accountKey);
			ps.setInt(2, assetKey);
			ps.setString(3, purchaseDate);
			ps.setDouble(4, purchaseExchangeRate);
			ps.setDouble(5, numberOfCoins);
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not add crypto to account", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a property asset corresponding to <code>assetCode</code> (which is
	 * assumed to already exist in the database) to the account corresponding to the
	 * provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchasePrice
	 */
	public static void addPropertyToAccount(String accountNumber, String assetCode, String purchaseDate,
			double purchasePrice) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		int accountKey = getAccountId(accountNumber);
		int assetKey = getAssetId(assetCode);
		try {
			String query = "insert into AccountAsset (accountId, assetId, purchaseDate, purchasePrice) values"
					+ "(?, ?, ?, ?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, accountKey);
			ps.setInt(2, assetKey);
			ps.setString(3, purchaseDate);
			ps.setDouble(4, purchasePrice);
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not add property to the account", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a stock asset corresponding to <code>assetCode</code> (which is assumed
	 * to already exist in the database) to the account corresponding to the
	 * provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchaseSharePrice
	 * @param numberOfShares
	 * @param dividendTotal
	 */
	public static void addStockToAccount(String accountNumber, String assetCode, String purchaseDate,
			double purchaseSharePrice, double numberOfShares, double dividendTotal) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		int accountKey = getAccountId(accountNumber);
		int assetKey = getAssetId(assetCode);
		try {
			String query = "insert into AccountAsset (accountId, assetId, purchaseDate, purchaseSharePrice, numberOfShares, dividendTotal) values"
					+ "(?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, accountKey);
			ps.setInt(2, assetKey);
			ps.setString(3, purchaseDate);
			ps.setDouble(4, purchaseSharePrice);
			ps.setDouble(5, numberOfShares);
			ps.setDouble(6, dividendTotal);
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not add stock to account", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Adds a stock option asset corresponding to <code>assetCode</code> (which is
	 * assumed to already exist in the database) to the account corresponding to the
	 * provided <code>accountNumber</code>.
	 *
	 * @param accountNumber
	 * @param assetCode
	 * @param purchaseDate
	 * @param purchaseSharePrice
	 * @param numberOfShares
	 * @param dividendTotal
	 */
	public static void addStockOptionToAccount(String accountNumber, String assetCode, String purchaseDate,
			String optionType, String strikeDate, double shareLimit, double premiumPerShare,
			double strikePricePerShare) {
		Connection conn = DatabaseUtils.establishConnection();
		PreparedStatement ps = null;
		int accountKey = getAccountId(accountNumber);
		int assetKey = getAssetId(assetCode);
		try {
			String query = "insert into AccountAsset (accountId, assetId, purchaseDate, optionType, strikeDate, shareLimit, premium, strikePrice) values"
					+ "(?, ?, ?, ?, ?, ?, ?, ?);";
			ps = conn.prepareStatement(query);
			ps.setInt(1, accountKey);
			ps.setInt(2, assetKey);
			ps.setString(3, purchaseDate);
			ps.setString(4, optionType);
			ps.setString(5, strikeDate);
			ps.setDouble(6, shareLimit);
			ps.setDouble(7, premiumPerShare);
			ps.setDouble(8, strikePricePerShare);
			ps.executeUpdate();
		} catch (SQLException e) {
			LOG.error("Could not add stock option to account", e);
			throw new RuntimeException(e);
		}
		try {
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
	}
}
