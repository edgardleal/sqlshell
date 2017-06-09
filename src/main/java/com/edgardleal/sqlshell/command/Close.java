/**
 * 
 */
package com.edgardleal.sqlshell.command;

import com.edgardleal.sqlshell.ConnectionFactory;

/**
 * @author Edgard Leal
 *
 */
public class Close implements ICommand {

	/* (non-Javadoc)
	 * @see com.edgardleal.sqlshell.command.ICommand#execute(java.lang.String[])
	 */
	@Override
	public String execute(String... args) {
		ConnectionFactory.close();
		return null;
	}

}
