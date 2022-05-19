package com.iffi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import com.iffi.accounts.Account;
import com.iffi.accounts.Person;

/**
 * Given a list of people & a list of assets in the database, this method loads
 * in account data from the database and syncs it to the people and assets.
 * 
 * @author ebrown
 *
 */
public class AccountLoader {

	public static final Logger LOG = LogManager.getLogger(AccountLoader.class);

	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}

	/**
	 * Returns a list of accounts with associated people.
	 * 
	 * @param persons
	 * @param assets
	 */
	public static List<Account> getAllAccounts(List<Person> persons) {
		Account a = null;
		List<Account> result = new ArrayList<Account>();
		Connection conn = DatabaseUtils.establishConnection();
		String query = "select a.accountId, a.accountNumber, a.type, a.ownerId, a.managerId, a.beneficiaryId from Account a";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				int accountId = rs.getInt("accountId");
				String accountNumber = rs.getString("accountNumber");
				String type = rs.getString("type");
				int ownerId = rs.getInt("ownerId");
				int managerId = rs.getInt("managerId");
				int beneficiaryId = rs.getInt("beneficiaryId");
				Person owner = null;
				Person manager = null;
				Person beneficiary = null;
				// add people to respective accounts
				for (Person p : persons) {
					if (p.getPersonId() == ownerId) {
						owner = p;
					}
					if (p.getPersonId() == managerId) {
						manager = p;
					}
					if (p.getPersonId() == beneficiaryId) {
						beneficiary = p;
					}
				}
				a = new Account(accountId, accountNumber, type, owner, manager, beneficiary);
				result.add(a);
			}
		} catch (SQLException e) {
			LOG.error("Could not execute query", e);
			throw new RuntimeException(e);
		}
		try {
			if (!(rs == null)) {
			rs.close();
			ps.close();
			conn.close();
			}
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
		return result;
	}
}
