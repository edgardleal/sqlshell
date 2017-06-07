package com.edgardleal.sqlshell;


import com.edgardleal.config.Config;
import com.edgardleal.log.Log;
import com.edgardleal.log.LogFactory;
import java.io.FileNotFoundException;
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
import org.slf4j.LoggerFactory;


/**
 * <p>
 * ConnectionFactory class.
 * </p>
 *
 * @author edgardleal
 * @version $Id: $Id
 */
public final class ConnectionFactory {

  public static final String CONNECTION_URL = "connectionURL";
  private static Connection conn = null;
  static String oracle_driver = "oracle.jdbc.driver.OracleDriver";
  static String mysql_driver = "com.mysql.jdbc.Driver";
  private static HashMap<String, Statement> statements = new HashMap<>();
  /** Constant <code>logger</code> */
  public static final Log LOGGER = LogFactory.getLog(ConnectionFactory.class);

  private ConnectionFactory() {

  }

  /**
   * <p>
   * closeStatement.
   * </p>
   *
   * @param statement a {@link Statement} object.
   */
  public static void closeStatement(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      statement = null;
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
   * @param sql a {@link java.lang.String} object.
   * @return a {@link java.sql.PreparedStatement} object.
   * @throws java.sql.SQLException if any.
   * @throws java.io.IOException if any.
   * @throws java.io.FileNotFoundException if any.
   */
  public static PreparedStatement getStatement(final String sql) throws SQLException,
      FileNotFoundException, IOException {
    PreparedStatement stm = (PreparedStatement) statements.get(sql);
    if (stm == null) {
      LOGGER.debug("Criando um statement para a query: ", sql);
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
   * @throws IOException 
   * @throws FileNotFoundException 
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

      try {
        conn =
            DriverManager.getConnection(Config.getDb("url"), Config.getDb("user"),
                Config.getDb("pass"));
        conn.setAutoCommit(false);
      } catch (SQLException e) {
        LOGGER.error("Erro ao se conectar com o banco de dados", e);

      }

      return conn;
    }
  }

  /**
   * <p>
   * getConnection.
   * </p>
   *
   * @return a {@link java.sql.Connection} object.
   * @throws java.io.IOException if any.
   * @throws java.io.FileNotFoundException if any.
   * @throws java.sql.SQLException if any.
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
