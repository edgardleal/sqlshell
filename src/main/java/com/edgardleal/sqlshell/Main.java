package com.edgardleal.sqlshell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edgardleal.sqlshell.render.Render;

public class Main {
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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
      try (ResultSet resultSet = ConnectionFactory.getStatement(builder.toString()).executeQuery()) {
        Render render = Config.getRender();
        render.renderResultSet(resultSet);
      } catch (SQLException se) {
        System.out.println(String.format("Erro ao executar o comando: [%s]", builder.toString()));
        se.printStackTrace();
      }catch(Exception ex){
        System.out.println("");
        ex.printStackTrace();
        
      }
    }

    ConnectionFactory.close();

    LOGGER.info(DurationFormatUtils.formatDurationISO(System.currentTimeMillis() - start));
  }

}
