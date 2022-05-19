package com.iffi.database;

/**
 * Info to connect to database hosted on CSE
 */
public class DatabaseCredentials {

	public static final String PARAMETERS = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public static final String USERNAME = "ebrown";
	public static final String PASSWORD = "oCICFvezKeNPXrgnGPB2";
	public static final String URL = "jdbc:mysql://cse.unl.edu/" + USERNAME + PARAMETERS;
}
