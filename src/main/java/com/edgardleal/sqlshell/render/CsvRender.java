package com.edgardleal.sqlshell.render;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;

public class CsvRender implements Render {

  public CsvRender() {}

  @Override
  public void renderResultSet(ResultSet resultSet) {
    try {
      CSVFormat.EXCEL.withHeader(resultSet).print(System.out).printRecords(resultSet);
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

}
