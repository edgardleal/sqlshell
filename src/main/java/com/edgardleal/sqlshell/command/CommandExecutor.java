package com.edgardleal.sqlshell.command;

import org.apache.commons.lang3.StringUtils;

/**
 * <p> </p>
 *
 * @author Edgard Leal <edgardleal@gmail.com>
 * @since 6/7/2017 1:53 PM
 */
public class CommandExecutor {

  public boolean execute(final String... args) {
    try {
      String className = StringUtils.capitalize(args[0]);
      ICommand command = (ICommand) Class.forName("com.edgardleal.sqlshell.command." + className).newInstance();
      command.execute(args);
      return true;
    } catch (ClassNotFoundException e) {

    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    }

    return false;
  }
}
