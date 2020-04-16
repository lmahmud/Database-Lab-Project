package de.unidue.inf.is.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database connection pool is used, instead of creating new connection every time.
 * Connection pool allows to reuse connections, hence it's efficient.
 * @author kt
 */
@WebListener
public class DBUtil implements ServletContextListener {
  private static DataSource hds;

  public void contextInitialized(ServletContextEvent sce) {
    try {
      InitialContext ic = new InitialContext();
      hds = (DataSource) ic.lookup("java:comp/env/jdbc/db2ds");
    } catch (NamingException e) {
      System.err.println("CRITICAL ERROR: Jetty couldn't initialize database. ");
    }
  }

  public static Connection getConnection() throws SQLException {
    if (hds == null) {
      throw new SQLException("CRITICAL ERROR: Jetty couldn't initialize database. ");
    }
    return hds.getConnection();
  }
}
