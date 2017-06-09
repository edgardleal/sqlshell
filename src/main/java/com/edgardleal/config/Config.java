package com.edgardleal.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import com.edgardleal.log.Log;
import com.edgardleal.log.LogFactory;
import com.edgardleal.sqlshell.render.Render;

public class Config {

  private static final Log LOGGER = LogFactory.getLog(Config.class);
  private static final File USER_CONFIG_FILE =
      new File(SystemUtils.USER_HOME, ".sqlshell.properties");
  private static final File LOCAL_CONFIG_FILE = new File("config.properties");
  private static Properties properties = null;
  private static CommandLine commandLineOptions;

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
    return get(key, StringUtils.EMPTY);
  }

  public static String getEnv(final String key, final String def) {
    String result = System.getenv(key);
    if (StringUtils.isBlank(result)) {
      result = def;
    }

    return result;
  }

  public static boolean hasOption(char key) {
    return commandLineOptions != null && commandLineOptions.hasOption(key);
  }

  public static String get(String key, String def) {
    String result =
        commandLineOptions != null ? commandLineOptions.getOptionValue(key) : StringUtils.EMPTY;
    try {
      if (StringUtils.isBlank(result)) {
        result = getProperties().getProperty(key, getEnv(key, StringUtils.EMPTY));
      }
      if (StringUtils.isBlank(result)) {
        result = def;
      }
    } catch (IOException e) {
      result = def;
    }

    return result;
  }

  public static String getDb(final String key, final String def)
      throws FileNotFoundException, IOException {
    final String finalKey = String.format("%s.%s", get("datasource"), key);
    final String result = get(key, get(finalKey, def));
    LOGGER.debug("config.database.key.value", key, result);
    return result;
  }

  public static String getDb(String key) throws FileNotFoundException, IOException {
    return getDb(key, StringUtils.EMPTY);
  }

  public static Render getRender() throws FileNotFoundException, IOException,
      InstantiationException, IllegalAccessException, ClassNotFoundException {
    Render render;
    String className = get("render", getDb("render", "console"));
    className =
        String.format("com.edgardleal.sqlshell.render.%sRender", StringUtils.capitalize(className));
    render = (Render) Class.forName(className).newInstance();

    return render;
  }

  public static void loadArgs(String... args) {
    commandLineOptions = CommandLineConfig.create(args);
  }

}
