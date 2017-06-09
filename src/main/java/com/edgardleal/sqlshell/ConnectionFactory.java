package com.edgardleal.sqlshell;


import com.edgardleal.config.Config;
import com.edgardleal.log.Log;
import com.edgardleal.log.LogFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;


/**
 * <p>
 * ConnectionFactory class.
 * </p>
 *
 * @author edgardleal
 * @version $Id: $Id
 */
public final class ConnectionFactory {

  /**
   * Constant <code>logger</code>
   */
  public static final Log LOGGER = LogFactory.getLog(ConnectionFactory.class);
  private static Connection conn = null;
  private static HashMap<String, Statement> statements = new HashMap<>();

  private ConnectionFactory() {
    // this class is a singleton
  }

  /**
   * <p>
   * closeStatement.
   * </p>
   *
   * @param statement a {@link Statement} object.
   */
  public static void closeStatement(final Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * <p>
   * close.
   * </p>
   */
  public static void close() {
    try {

      Iterator<Entry<String, Statement>> iterator = statements.entrySet().iterator();
      LOGGER.debug("database.statement.closing", statements.size());

      while (iterator.hasNext()) {
        try {
          iterator.next().getValue().close();
        } catch (Exception e) {
          LOGGER.error("error.database.statement.closing", e);
        }
      }

      if (conn != null) {
        conn.close();
      }
      LOGGER.debug("database.connection.all.closed");
      conn = null;
      statements = new HashMap<>();
    } catch (Exception e) {
      LOGGER.error("error.database.closing", e);
    }
  }

  /**
   * <p>
   * getStatement.
   * </p>
   *
   * @param sql a {@link String} object.
   * @return a {@link PreparedStatement} object.
   * @throws SQLException if any.
   * @throws IOException if any.
   */
  public static PreparedStatement getStatement(final String sql) throws SQLException, IOException {
    PreparedStatement stm = (PreparedStatement) statements.get(sql);
    if (stm == null) {
      LOGGER.debug("database.statement.query", sql);
      stm = getConnection().prepareStatement(sql);
      statements.put(sql, stm);
    }
    return stm;
  }

  /**
   * <p>
   * createConnection.
   * </p>
   *
   * @return a {@link java.sql.Connection} object.
   */
  public static synchronized Connection createConnection() throws IOException {
    if (conn != null) {
      return conn;
    } else {
      String driver = StringUtils.EMPTY;
      try {
        driver = Config.getDb("driver");
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        LOGGER.error("error.database.driver.not.found", driver);
      }
      String url = Config.getDb("url"), user = Config.getDb("user"), pass = Config.getDb("pass");

      try {
        conn = DriverManager.getConnection(url, user, pass);
        conn.setAutoCommit(false);
      } catch (SQLException e) {
        LOGGER.error("error.database.connecting", e);

      }

      return conn;
    }
  }

  /**
   * <p>
   * getConnection.
   * </p>
   *
   * @return a {@link Connection} object.
   * @throws IOException if any.
   * @throws SQLException if any.
   */
  public static Connection getConnection() throws IOException, SQLException {
    if (conn == null) {
      TimeZone timeZone = TimeZone.getTimeZone("UTC+03:00");
      TimeZone.setDefault(timeZone);
      LOGGER.debug("database.connecting");
      createConnection();
      conn.setAutoCommit(false);
    }
    return conn;
  }

  public static void rollback() {
    try {
      getConnection().rollback();
    } catch (SQLException | IOException e) {
      LOGGER.error("Erro ao realizar o rollback da transacao", e);
    }
  }

}
