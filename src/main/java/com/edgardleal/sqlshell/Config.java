package com.edgardleal.sqlshell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

public class Config {

  static Properties properties = null;
  public static String current_connection;
  private static final File USER_CONFIG_FILE = new File(SystemUtils.USER_HOME,
      ".sqlshell.properties");
  private static final File LOCAL_CONFIG_FILE = new File("config.properties");

  private static Properties getProperties() throws FileNotFoundException, IOException {
    if (properties == null) {
      properties = new Properties();
      if (USER_CONFIG_FILE.exists()) {
        properties.load(new FileInputStream(USER_CONFIG_FILE));
      } else {
        properties.load(new FileInputStream(LOCAL_CONFIG_FILE));
      }
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
