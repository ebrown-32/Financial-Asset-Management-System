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

import com.iffi.accounts.Address;
import com.iffi.accounts.Person;

/**
 * Class to load people from a database.
 * 
 * @author ebrown
 *
 */
public class PersonLoader {

	public static final Logger LOG = LogManager.getLogger(PersonLoader.class);

	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}

	/**
	 * Returns an Address object based on it's ID (pKey) in the database
	 * 
	 * @param addressId
	 */
	public static Address getAddressById(int addressId) {
		Address a = null;
		Connection conn = DatabaseUtils.establishConnection();
		String query = "select a.street as `Street`, a.city as `City`, a.zip as `Zip`, s.state as `State`, c.country as `Country` from Address a\r\n"
				+ "join State s on s.stateId = a.stateId\r\n" + "join Country c on c.countryId = a.countryId\r\n"
				+ "where a.addressId = ?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			ps.setInt(1, addressId);

			rs = ps.executeQuery();

			while (rs.next()) {
				String street = rs.getString("Street");
				String city = rs.getString("City");
				String zip = rs.getString("Zip");
				String state = rs.getString("State");
				String country = rs.getString("Country");
				a = new Address(street, city, state, zip, country);
			}
		} catch (SQLException e) {
			LOG.error("Could not execute query", e);
			throw new RuntimeException(e);
		}
		try {
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
		return a;
	}

	/**
	 * Retrieves all Person entires stored in the 'Person' table.
	 * 
	 * @return A list of Person objects found in the database.
	 */
	public static List<Person> getAllPersons() {
		// retrieve persons without emails
		List<Person> result = new ArrayList<Person>();
		Person p = null;
		Connection conn = DatabaseUtils.establishConnection();
		String query = "select p.personId, p.personCode as `Code`, p.lastName as `Last`, p.firstName as `First`, a.addressId from Person p\r\n"
				+ "join Address a on a.addressId = p.addressId";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			rs = ps.executeQuery();
			// create people/persons without e-mail(s)
			while (rs.next()) {
				int personId = rs.getInt("personId");
				String code = rs.getString("Code");
				String lastName = rs.getString("Last");
				String firstName = rs.getString("First");
				int addressId = rs.getInt("addressId");
				Address a = getAddressById(addressId);
				p = new Person(personId, code, lastName, firstName, a);
				result.add(p);
			}
		} catch (SQLException e) {
			LOG.error("Could not execute query", e);
			throw new RuntimeException(e);
		}
		try {
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException e) {
			LOG.error("Could not close resources", e);
			throw new RuntimeException(e);
		}
		// add e-mails to each person
		query = "select p.personCode, e.emailAddress from Person p\r\n" + "join Email e on e.personId = p.personId\r\n"
				+ "where personCode = ?";
		for (Person person : result) {
			conn = DatabaseUtils.establishConnection();
			ps = null;
			rs = null;
			try {
				ps = conn.prepareStatement(query);
				ps.setString(1, person.getPersonCode());
				rs = ps.executeQuery();
				while (rs.next()) {
					String emailAddress = rs.getString("emailAddress");
					person.addEmail(emailAddress);
				}
			} catch (SQLException e) {
				LOG.error("Could not execute query", e);
				throw new RuntimeException(e);
			}
			try {
				rs.close();
				ps.close();
				conn.close();
			} catch (SQLException e) {
				LOG.error("Could not execute resources", e);
				throw new RuntimeException(e);
			}
		}
		return result;
	}
}
