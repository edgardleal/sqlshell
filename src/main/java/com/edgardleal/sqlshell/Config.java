package com.edgardleal.sqlshell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {

  static Properties properties = null;
  public static String current_connection;

  private static Properties getProperties() throws FileNotFoundException, IOException {
    if (properties == null) {
      properties = new Properties();
      properties.load(new FileInputStream(new File("config.properties")));
    }

    return properties;
  }

  public static String get(String key) throws FileNotFoundException, IOException {
    return getProperties().getProperty(key);
  }

  public static String get(String key, String def) {
    String result;
    try {
      result = getProperties().getProperty(key);
      if (result == null || result.equals("")) {
        result = def;
      }
    } catch (IOException e) {
      result = def;
    }
    return result;
  }

  public static String getDb(String key) throws FileNotFoundException, IOException {
    return get(String.format("%s.%s", current_connection, key));
  }

}
