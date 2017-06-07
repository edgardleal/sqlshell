package com.edgardleal.sqlshell;

import java.sql.ResultSet;

public interface Render {

  void renderResultSet(ResultSet resultSet);
  void renderBoolean(boolean value);
}
