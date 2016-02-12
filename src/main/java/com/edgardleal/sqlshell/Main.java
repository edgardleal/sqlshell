package com.edgardleal.sqlshell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {

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
      try (ResultSet resultSet = ConnectionFactory.getStatement(builder.toString()).executeQuery()) {
        Render render = new ConsoleRender();
        render.renderResultSet(resultSet);
      } catch (SQLException se) {
        System.out.println(String.format("Erro ao executar o comando: [%s]", builder.toString()));
        se.printStackTrace();
      }
    }

    ConnectionFactory.close();
  }

}
