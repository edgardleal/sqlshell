package com.edgardleal.sqlshell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.edgardleal.sqlshell.render.Render;

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

  public static String getEnv(final String key, final String def) {
    String result = System.getenv(key);
    if (StringUtils.isBlank(result)) {
      result = def;
    }

    return result;
  }

  public static String get(String key, String def) {
    String result;
    try {
      result = getProperties().getProperty(key, getEnv(key, StringUtils.EMPTY));
      if (StringUtils.isBlank(result)) {
        result = def;
      }
    } catch (IOException e) {
      result = def;
    }

    return result;
  }

  public static String getDb(final String key, final String def) throws FileNotFoundException,
      IOException {
    return get(String.format("%s.%s", current_connection, key), def);
  }

  public static String getDb(String key) throws FileNotFoundException, IOException {
    return getDb(key, StringUtils.EMPTY);
  }

  public static Render getRender() throws FileNotFoundException, IOException,
      InstantiationException, IllegalAccessException, ClassNotFoundException {
    Render render;
    String className = getDb("render", get("render", "console"));
    className =
        String.format("com.edgardleal.sqlshell.render.%sRender", StringUtils.capitalize(className));
    render = (Render) Class.forName(className).newInstance();

    return render;
  }

}
