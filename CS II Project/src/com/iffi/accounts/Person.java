package com.iffi.accounts;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models a person.
 * 
 * @author ebrown
 *
 */
public class Person {

	private final int personId;
	private final String personCode;
	private final String lastName;
	private final String firstName;
	private final Address address;
	private final List<String> emails;

	@Override
	public String toString() {
		return lastName + ", " + firstName + "\n" + emails + "\n" + address + "\n";
	}

	public Person(String personCode, String lastName, String firstName, Address address, List<String> emails) {
		super();
		this.personId = 0;
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		this.emails = emails;
	}

	public Person(int personId, String personCode, String lastName, String firstName, Address address) {
		super();
		this.personId = personId;
		this.personCode = personCode;
		this.lastName = lastName;
		this.firstName = firstName;
		this.address = address;
		this.emails = new ArrayList<>();
	}

	/**
	 * Add an email to a person's email(s)
	 * 
	 * @param email
	 */
	public void addEmail(String email) {
		this.emails.add(email);
	}

	public String getPersonCode() {
		return personCode;
	}

	public String getName() {
		return lastName + ", " + firstName;
	}

	public Address getAddress() {
		return address;
	}

	public List<String> getEmails() {
		return emails;
	}

	public int getPersonId() {
		return personId;
	}
}
