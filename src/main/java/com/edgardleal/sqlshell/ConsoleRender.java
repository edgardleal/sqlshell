package com.edgardleal.sqlshell;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

public class ConsoleRender implements Render {

  private static final int MAX_CONTENT_WIDTH = 35;

  private String npad(char value, int times) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < times; i++) {
      builder.append(value);
    }

    return builder.toString();
  }

  @Override
  public void renderResultSet(ResultSet resultSet) {

    StringBuilder data = new StringBuilder();
    StringBuilder tableHeader = new StringBuilder();
    try {
      ResultSetMetaData metaData = resultSet.getMetaData();

      /**
       * Mascaras para imprmmir os valores com tamanho fixo
       */
      String[] masks = new String[metaData.getColumnCount()];
      int[] widths = new int[masks.length];
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        if (i == 1) {
          data.append("| ");
          tableHeader.append("+-");
        } else {
          tableHeader.append('-');
        }
        widths[i - 1] =
            Math.max(Math.min(metaData.getColumnDisplaySize(i), MAX_CONTENT_WIDTH), metaData
                .getColumnName(i).length());
        masks[i - 1] = String.format("%%-%ds", widths[i - 1]);

        tableHeader.append(npad('-', widths[i - 1]) + "-+");
        data.append(String.format(masks[i - 1], metaData.getColumnName(i)) + " | ");
      }
      data.append('\n');
      /**
       * Imprimi separador dos titulo para os dados
       */
      for (int i = 0; i < masks.length; i++) {
        if (i == 0) {
          data.append("+-");
        } else {
          data.append('-');
        }
        data.append(String.format("%s+", npad('-', widths[i] + 1)));
      }

      while (resultSet.next()) {

        data.append('\n');
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          if (i == 1) {
            data.append("| ");
          }
          // masks[i - 1] = String.format("%%-%ds", metaData.getColumnDisplaySize(i));
          data.append(String.format(
              masks[i - 1],
              StringUtils.abbreviate(resultSet.getString(i),
                  Math.min(Math.max(4, widths[i - 1]), MAX_CONTENT_WIDTH - 3)))
              + " | ");
        }
      }
      System.out.println(tableHeader.toString());
      System.out.println(data.toString());
      System.out.println(tableHeader.toString());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void renderBoolean(boolean value) {
    StringBuilder builder = new StringBuilder();
    builder.append("+-----------+\n")
        //
        .append("| RESULT    |\n")
        //
        .append("+-----------+\n")
        //
        .append("| ").append(value ? "TRUE " : "FALSE").append("     |\n")
        .append("+----------------------------------------+\n");

    System.out.println(builder.toString());

  }

}
