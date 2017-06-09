package com.edgardleal.sqlshell;

import com.edgardleal.config.CommandLineConfig;
import com.edgardleal.config.Config;
import com.edgardleal.sqlshell.command.CommandExecutor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edgardleal.sqlshell.render.Render;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static void processUpdate(String command) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    try (PreparedStatement statement = ConnectionFactory.getStatement(command)) {
      Render render = Config.getRender();
      render.renderBoolean(statement.execute());
    } catch (SQLException se) {
      System.out.println(String.format("Erro ao executar o comando: [%s]", command));
      se.printStackTrace();
    }
  }

  public static void processSelect(final String command) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    try (ResultSet resultSet = ConnectionFactory.getStatement(command).executeQuery()) {
      Render render = Config.getRender();
      render.renderResultSet(resultSet);
    } catch (SQLException se) {
      System.out.println(String.format("Erro ao executar o comando: [%s]", command));
      se.printStackTrace();
    }
  }

  public static void main(String[] args) throws FileNotFoundException, IOException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

    long start = System.currentTimeMillis();
    long memoryStart = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();

    Config.loadArgs(args);
    if(Config.hasOption('h')) {
      CommandLineConfig.printUsage();
      System.exit(0);
    }
    if (new CommandExecutor().execute(args)) {
      return;
    }
    StringBuilder builder = new StringBuilder();

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
      if (command.toString().replaceAll("[\n\t\t]+", StringUtils.EMPTY).toUpperCase()
          .indexOf("SELECT") == 0) {
        processSelect(builder.toString());
      } else {
        processUpdate(command);
      }
    }

    ConnectionFactory.close();

    printExecutionInfo(start, memoryStart);
  }

  private static void printExecutionInfo(long start, long memoryStart) {
    LOGGER.info(DurationFormatUtils.formatDuration(System.currentTimeMillis() - start, "s.S"));
    long usedMemory = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
    usedMemory = usedMemory - memoryStart;
    LOGGER.info((usedMemory / 1024 / 1024) + "MB");
  }

}
