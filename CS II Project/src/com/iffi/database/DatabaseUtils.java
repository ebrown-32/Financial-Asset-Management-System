package com.iffi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;


/**
 * Utility methods useful for JDBC.
 * @author ebrown
 *
 */
public class DatabaseUtils {
	
	public static final Logger LOG = LogManager.getLogger(DatabaseUtils.class);
	
	static {
		Configurator.initialize(new DefaultConfiguration());
		Configurator.setRootLevel(Level.INFO);
	}
	
	/*
	 * Factory method for establishing a connection to database.
	 */
	public static Connection establishConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DatabaseCredentials.URL, DatabaseCredentials.USERNAME,
					DatabaseCredentials.PASSWORD);
		} catch (SQLException e) {
			LOG.error("Could not establish connection", e);
			throw new RuntimeException(e);
		}
		return conn;
	}
}
