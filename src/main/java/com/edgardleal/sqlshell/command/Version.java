package com.edgardleal.sqlshell.command;

import com.edgardleal.sqlshell.AppInfo;

/**
 * <p> </p>
 *
 * @author Edgard Leal <edgardleal@gmail.com>
 * @since 6/7/2017 1:52 PM
 */
public class Version implements ICommand {

  @Override
  public String execute(String... args) {
    System.out.println(AppInfo.VERSION);
    return null;
  }
}
