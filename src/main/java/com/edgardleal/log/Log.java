package com.edgardleal.log;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p> </p>
 *
 * @author Edgard Leal <edgardleal@gmail.com>
 * @since 6/6/2017 4:29 PM
 */
public class Log {

  private Logger logger;
  private ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

  public Log(Class<?> clazz) {
    this.logger = LoggerFactory.getLogger(clazz);
  }

  String translate(final String message) {
    return resourceBundle.getString(message);
  }

  String formatt(final String message, Object... args) {
    MessageFormat messageFormat = new MessageFormat(message);
    return messageFormat.format(args);
  }

  public void debug(final String message, Object... args) {
    String translated = resourceBundle.getString(message);
    if (StringUtils.isBlank(translated)) {
      translated = message;
    }
    logger.debug(formatt(translated, args));
  }

  public boolean isDebugEnabled() {
    return logger.isDebugEnabled();
  }

  public void error(String message, Exception e) {
    logger.error(translate(message), e);
  }

  public void error(String message, String... args) {
    logger.error(formatt(translate(message), args));
  }
}
