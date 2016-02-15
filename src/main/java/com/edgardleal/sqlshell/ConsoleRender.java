package com.edgardleal.sqlshell;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

public class ConsoleRender implements Render {

  private static final int MAX_CONTENT_WIDTH = 40;

  private String npad(char value, int times) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < times; i++) {
      builder.append(value);
    }

    return builder.toString();
  }

  @Override
  public void renderResultSet(ResultSet resultSet) {

    System.out.println(npad('-', 50));
    try {
      ResultSetMetaData metaData = resultSet.getMetaData();

      /**
       * Mascaras para imprmmir os valores com tamanho fixo
       */
      String[] masks = new String[metaData.getColumnCount()];
      int[] widths = new int[masks.length];
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        if (i == 1) {
          System.out.print("| ");
        }
        widths[i - 1] =
            Math.max(Math.min(metaData.getColumnDisplaySize(i), MAX_CONTENT_WIDTH), metaData
                .getColumnName(i).length());
        masks[i - 1] = String.format("%%-%ds", widths[i - 1]);
        System.out.print(String.format(masks[i - 1], metaData.getColumnName(i)) + " | ");
      }
      System.out.println();
      /**
       * Imprimi separador dos titulo para os dados
       */
      for (int i = 0; i < masks.length; i++) {
        if (i == 0) {
          System.out.print("+-");
        }
        System.out.print(String.format("%s-+", npad('-', widths[i] + 1)));
      }

      System.out.println();
      while (resultSet.next()) {

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          if (i == 1) {
            System.out.print("| ");
          }
          // masks[i - 1] = String.format("%%-%ds", metaData.getColumnDisplaySize(i));
          System.out.print(String.format(masks[i - 1],
              StringUtils.abbreviate(resultSet.getString(i), MAX_CONTENT_WIDTH))
              + " | ");
        }
        System.out.println();

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println(npad('-', 50));
  }

}
