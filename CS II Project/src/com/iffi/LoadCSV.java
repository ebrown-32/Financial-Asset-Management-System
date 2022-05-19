package com.iffi;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.iffi.accounts.Account;
import com.iffi.accounts.Address;
import com.iffi.accounts.Person;
import com.iffi.assets.Asset;
import com.iffi.assets.Call;
import com.iffi.assets.Cryptocurrency;
import com.iffi.assets.Property;
import com.iffi.assets.Put;
import com.iffi.assets.Stock;

public class LoadCSV {

	/**
	 * Loads and processes an 'Accounts.csv' file located in the data directory.
	 * Creates a new list of accounts, with the account information corresponding to 
	 * people and assets from the lists passed in as parameters.
	 * @param people
	 * @param assets
	 * @return accounts
	 */
	public static List<Account> parseAccountsDataFile(List<Person> people, List<Asset> assets) {
		List<Account> accounts = new ArrayList<Account>();
		try (Scanner s = new Scanner(new File("data/Accounts.csv"))) {
			String line = s.nextLine();
			int records = Integer.parseInt(line);
			int n = 0;
			while (n < records) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				String accountNum = tokens[0];
				String accountType = tokens[1];
				Person owner = null;
				Person manager = null;
				Person beneficiary = null;
				
				//assign the people to each account
				for (Person p : people) {
					if (p.getPersonCode().compareTo(tokens[2]) == 0) {
						owner = p;
					}
					if (p.getPersonCode().compareTo(tokens[3]) == 0) {
						manager = p;
					}
					if (p.getPersonCode().compareTo(tokens[4]) == 0) {
						beneficiary = p;
					}
				}
				
				//create a new account for each loop through
				Account acct = new Account(accountNum, accountType, owner, manager, beneficiary);
				
				//utilize method overloading and constructors to create new assets belonging to each account, merging existing asset records to ones in the account data
				for (int i = 5; i < tokens.length; i++) {
					String str = tokens[i];
					for (Asset a : assets) {
						if (a.getCode().compareTo(str) == 0) {
							String assetType = a.getType();
							if (assetType.compareTo("Property") == 0) {
								Property prop = new Property((Property)a, LocalDate.parse(tokens[i+1]), Double.parseDouble(tokens[i+2]));
								acct.addAsset(prop);
							}
							if (assetType.compareTo("Cryptocurrency") == 0) {
								Cryptocurrency c = new Cryptocurrency((Cryptocurrency)a, LocalDate.parse(tokens[i+1]), Double.parseDouble(tokens[i+2]), Double.parseDouble(tokens[i+3]));
								acct.addAsset(c);
							}
							if (assetType.compareTo("Stock") == 0) {
								if (tokens[i+1].compareTo("S") == 0) {
									Stock stock = new Stock((Stock)a, LocalDate.parse(tokens[i+2]), Double.parseDouble(tokens[i+3]), Double.parseDouble(tokens[i+4]), Double.parseDouble(tokens[i+5]));
									acct.addAsset(stock);
								}
								if (tokens[i+1].compareTo("P") == 0) {
									Put p = new Put((Stock)a, LocalDate.parse(tokens[i+2]), Double.parseDouble(tokens[i+3]), Double.parseDouble(tokens[i+4]), Double.parseDouble(tokens[i+5]), LocalDate.parse(tokens[i+6]));
									acct.addAsset(p);
								}
								if (tokens[i+1].compareTo("C") == 0) {
									Call c = new Call((Stock)a, LocalDate.parse(tokens[i+2]), Double.parseDouble(tokens[i+3]), Double.parseDouble(tokens[i+4]), Double.parseDouble(tokens[i+5]), LocalDate.parse(tokens[i+6]));
									acct.addAsset(c);
								}	
							}
						}
					}
				}	
				accounts.add(acct);  
				n++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return accounts;
	}
	
	
	/**
	 * Loads and parses a csv file containing attributes of a person. Returns a list
	 * of "Person" objects.
	 * 
	 * @return
	 */
	public static List<Person> parsePersonsDataFile() {
		List<Person> result = new ArrayList<Person>();
		try (Scanner s = new Scanner(new File("data/Persons.csv"))) {
			String line = s.nextLine();
			while (s.hasNextLine()) {
				line = s.nextLine();
				String tokens[] = line.split(",");
				String personCode = tokens[0];
				String lastName = tokens[1];
				String firstName = tokens[2];
				String street = tokens[3];
				String city = tokens[4];
				String state = tokens[5];
				String zip = tokens[6];
				String country = tokens[7];
				List<String> emails = new ArrayList<String>();
				for (int i = 8; i < tokens.length; i++) {
					emails.add(tokens[i]);
				}
				Address a = new Address(street, city, state, zip, country);
				Person p = new Person(personCode, lastName, firstName, a, emails);
				result.add(p);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Loads and parses a file containing records of different assets. Returns a
	 * list of "Assets"
	 * 
	 * @return
	 */
	public static List<Asset> parseAssetsDataFile() {
		List<Asset> result = new ArrayList<Asset>();
		try (Scanner s = new Scanner(new File("data/Assets.csv"))) {
			String line = s.nextLine();
			while (s.hasNextLine()) {
				line = s.nextLine();
				Asset a = null;
				String tokens[] = line.split(",");
				String code = tokens[0];
				String label = tokens[2];
				if (tokens[1].equals("P")) {
					double appraisedValue = Double.parseDouble(tokens[3]);
					a = new Property(code, "Property", label, appraisedValue);
				} else if (tokens[1].equals("S")) {
					String symbol = tokens[3];
					double sharePrice = Double.parseDouble(tokens[4]);
					a = new Stock(code, "Stock", label, symbol, sharePrice);
				} else if (tokens[1].equals("C")) {
					double exchangeRate = Double.parseDouble(tokens[3]);
					double exchangeFeeRate = Double.parseDouble(tokens[4]);
					a = new Cryptocurrency(code, "Cryptocurrency", label, exchangeRate, exchangeFeeRate);
				}
				result.add(a);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
}
