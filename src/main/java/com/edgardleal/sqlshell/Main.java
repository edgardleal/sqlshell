package com.edgardleal.sqlshell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static void processUpdate(String command) throws FileNotFoundException, IOException {
    try (PreparedStatement statement = ConnectionFactory.getStatement(command)) {
      Render render = new ConsoleRender();
      render.renderBoolean(statement.execute());
    } catch (SQLException se) {
      System.out.println(String.format("Erro ao executar o comando: [%s]", command));
      se.printStackTrace();
    }
  }

  public static void processSelect(final String command) throws FileNotFoundException, IOException {
    try (ResultSet resultSet = ConnectionFactory.getStatement(command).executeQuery()) {
      Render render = new ConsoleRender();
      render.renderResultSet(resultSet);
    } catch (SQLException se) {
      System.out.println(String.format("Erro ao executar o comando: [%s]", command));
      se.printStackTrace();
    }
  }

  public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {

    long start = System.currentTimeMillis();
    StringBuilder builder = new StringBuilder();

    Config.current_connection = args[0];
    if (args.length < 2) {
      String[] strings = Stdin.readAllStrings();
      for (String string : strings) {
        builder.append(string).append(' ');
      }
    }
    for (int i = 1; i < args.length; i++) {
      builder.append(args[i]).append(' ');
    }

    if (builder.length() > 0) {
      ConnectionFactory.getConnection();
      String command = builder.toString();
      if (command.toString().replaceAll("[\n\t\t]+", "").toUpperCase().indexOf("SELECT") == 0) {
        processSelect(builder.toString());
      } else {
        processUpdate(command);
      }
    }

    ConnectionFactory.close();

    LOGGER.info(DurationFormatUtils.formatDuration(System.currentTimeMillis() - start, "s.S"));
  }

}
