package com.edgardleal.sqlshell;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ConsoleRender implements Render {

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
      String[] masks = new String[metaData.getColumnCount()];
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        if (i == 1) {
          System.out.print("| ");
        }
        masks[i - 1] = String.format("%%-%ds", metaData.getColumnDisplaySize(i));
        System.out.print(String.format(masks[i - 1], metaData.getColumnName(i)) + " | ");
      }
      System.out.println();
      for (int i = 0; i < masks.length; i++) {
        if(i ==0){
          System.out.print("+-");
        }
        System.out.print(String.format("%s-+", npad('-', metaData.getColumnDisplaySize(i + 1))));
      }

      System.out.println();
      while (resultSet.next()) {

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          if (i == 1) {
            System.out.print("| ");
          }
          masks[i - 1] = String.format("%%-%ds", metaData.getColumnDisplaySize(i));
          System.out.print(String.format(masks[i - 1], resultSet.getString(i)) + " | ");
        }
        System.out.println();

      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    System.out.println(npad('-', 50));
  }

}
