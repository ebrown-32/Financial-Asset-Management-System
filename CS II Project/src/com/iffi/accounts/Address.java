package com.iffi.accounts;

/**
 * This class models an address
 * 
 * @author ebrown
 *
 */
public class Address {

	private final String street;
	private final String city;
	private final String state;
	private final String zip;
	private final String country;

	@Override
	public String toString() {
		return street + "\n" + city + ", " + state + " " + country + " " + zip;
	}

	public Address(String street, String city, String state, String zip, String country) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public String getCountry() {
		return country;
	}
}
