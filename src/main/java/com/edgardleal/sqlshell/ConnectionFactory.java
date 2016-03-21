package com.edgardleal.sqlshell;


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
  public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);

  private ConnectionFactory() {

  }

  /**
   * <p>
   * closeStatement.
   * </p>
   *
   * @param statement a {@link java.sql.Statement} object.
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
      LOGGER.debug("Fechando {} statements", statements.size());

      while (iterator.hasNext()) {
        try {
          iterator.next().getValue().close();
        } catch (Exception e) {
          LOGGER.error("Erro ao fechar o Statement", e);
        }
      }

      if (conn != null) {
        conn.close();
      }
      LOGGER.debug("All connections are closed.");
      conn = null;
      statements = new HashMap<String, Statement>();
    } catch (Exception e) {
      LOGGER.error("Erro ao fechar a conexao com o banco de dados", e);
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
      LOGGER.debug("Criando um statement para a query: ");
      LOGGER.debug(sql);
      stm = getConnection().prepareStatement(sql);
      statements.put(sql, stm);
    }
    return stm;
  }

  /**
   * <p>
   * obterConexao.
   * </p>
   *
   * @param prop a {@link java.util.Properties} object.
   * @return a {@link java.sql.Connection} object.
   * @throws IOException 
   * @throws FileNotFoundException 
   */
  public static synchronized Connection obterConexao() throws FileNotFoundException, IOException {
    if (conn != null) {
      return conn;
    } else {
      try {
        Class.forName(Config.getDb("driver"));
      } catch (ClassNotFoundException e) {
        LOGGER.error("Erro ao inicializar o driver de conexao com o banco de dados", e);
        LOGGER.error("Driver: [{}]", mysql_driver);
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
  public static Connection getConnection() throws IOException, FileNotFoundException, SQLException {
    if (conn == null) {
      TimeZone timeZone = TimeZone.getTimeZone("UTC+03:00");
      TimeZone.setDefault(timeZone);
      LOGGER.debug("Conectando-se ao banco de dados");
      obterConexao();
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
