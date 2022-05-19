package com.iffi;

import java.util.Comparator;
import java.util.List;

import com.iffi.accounts.Account;
import com.iffi.accounts.Person;
import com.iffi.assets.Asset;
import com.iffi.database.AccountLoader;
import com.iffi.database.AssetLoader;
import com.iffi.database.PersonLoader;


/**
 * Purpose: Main driver program to load csv files or from a database, each
 * containing "Persons," "Assets," and "Accounts" data. It syncs the data within
 * each into separate accounts with functionality according to financial
 * business rules.
 * 
 * @author ebrown Date: 2022/04/06
 */

public class AccountReport {

	public static void main(String[] args) {

		// load from database
		List<Person> people = PersonLoader.getAllPersons();
		List<Account> accounts = AccountLoader.getAllAccounts(people);
		AssetLoader.loadAllAssets(accounts);
		
		//comparators 
		Comparator<Account> compareByOwner = new Comparator<Account>() {

			@Override
			public int compare(Account a1, Account a2) {
				return (a1.getOwner().getName()).compareTo(a2.getOwner().getName());
			}
		};
		
		Comparator<Account> compareByManager = new Comparator<Account>() {

			@Override
			public int compare(Account a1, Account a2) {
				return (a1.getManager().getName()).compareTo(a2.getManager().getName());
			}
		};
		
		Comparator<Account> compareByValue = new Comparator<Account>() {

			@Override
			public int compare(Account a1, Account a2) {
				if (a1.getTotalValue() > a2.getTotalValue()) {
					return -1;
				}
				else if (a1.getTotalValue() < a2.getTotalValue()) {
					return 1;
				}
				else {
					return 0;
				}
			}
		};
		
		Comparator<Account> compareByReturnRate = new Comparator<Account>() {

			@Override
			public int compare(Account a1, Account a2) {
				if (a1.getTotalReturnPercentage() > a2.getTotalReturnPercentage()) {
					return -1;
				}
				else if (a1.getTotalReturnPercentage() < a2.getTotalReturnPercentage()) {
					return 1;
				}
				else {
					return 0;
				}
			}
		};
		
		//create instances of custom sortedlinkedlist implementation
		SortedLinkedList<Account> byOwnerList = new SortedLinkedList<Account>(compareByOwner);
		SortedLinkedList<Account> byManagerList = new SortedLinkedList<Account>(compareByManager);
		SortedLinkedList<Account> byValueList = new SortedLinkedList<Account>(compareByValue);
		SortedLinkedList<Account> byReturnList = new SortedLinkedList<Account>(compareByReturnRate);
		
		for (Account a : accounts) {
			byOwnerList.addElement(a);
			byManagerList.addElement(a);
			byValueList.addElement(a);
			byReturnList.addElement(a);
		}

		// account summary reports
		System.out.println("Account Summary Report By Owner");
		printSummaryReport(byOwnerList);
		System.out.println("Account Summary Report By Manager");
		printSummaryReport(byManagerList);
		System.out.println("Account Summary Report By Value");
		printSummaryReport(byValueList);
		System.out.println("Account Summary Report By Return");
		printSummaryReport(byReturnList);

		// account details report
		// printDetailReport(accounts);
	}
	
	public static void printSummaryReport(SortedLinkedList<Account> list) {
		System.out.println(
				"==============================================================================================================");
		System.out.printf("%s %20s %25s %15s %20s %17s %15s\n", "Account", "Owner", "Manager", "Fees", "Return", "Ret%",
				"Value");
		double totalFees = 0.0, totalReturn = 0.0, totalValue = 0.0;
		for (Account acct : list) {
			System.out.printf("%s %25s %25s %13.2f %18.2f %17.2f %14.2f\n", acct.getAccountNum(),
					acct.getOwner().getName(), acct.getManager().getName(), acct.getTotalFee(), acct.getTotalReturn(),
					acct.getTotalReturnPercentage(), acct.getTotalValue());
			totalFees += acct.getTotalFee();
			totalReturn += acct.getTotalReturn();
			totalValue += acct.getTotalValue();
		}
		System.out.printf("Totals $%63.2f \t  %.2f \t \t \t    %.2f\n\n", totalFees, totalReturn, totalValue);
	}
	
	public static void printDetailReport(List<Account> accounts) {
		System.out.println("Account Details Report");
		for (Account a : accounts) {
			System.out.println("====================");
			System.out.println(a);
			List<Asset> finalAssets = a.getAssets();
			for (Asset asset : finalAssets) {
				System.out.println(asset);
			}
			System.out.println("====================");
			System.out.printf("Total Value = $%.2f\n", a.getTotalValue());
			System.out.printf("Cost Basis  = $%.2f\n", a.getTotalCost());
			System.out.printf("Total Account Fees  = $%.2f\n", a.getTotalFee());
			System.out.printf("Total Return  = $%.2f\n", a.getTotalReturn());
			System.out.printf("Total Return %%  = %.2f%%\n", a.getTotalReturnPercentage());
			System.out.println("====================\n");
		}
	}
}
