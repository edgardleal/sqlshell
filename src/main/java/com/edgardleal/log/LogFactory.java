package com.edgardleal.log;

/**
 * <p> </p>
 *
 * @author Edgard Leal <edgardleal@gmail.com>
 * @since 6/6/2017 4:27 PM
 */
public class LogFactory {

  public static Log getLog(Class<?> clazz) {
     return new Log(clazz);
  }
}
