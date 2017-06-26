/**
 * 
 */
package com.edgardleal.config;

import java.io.PrintWriter;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Edgard Leal
 *
 */
public class CommandLineConfig {
  
  private static Options gnuOptions;

  /**
   * Construct and provide GNU-compatible Options.
   * 
   * @return Options expected from command-line of GNU form.
   */
  public static Options constructGnuOptions() {
    if (gnuOptions == null) {
      gnuOptions = new Options();

      gnuOptions
          .addOption("d", "datasource", true , "The name of datasource configured in ~/.sqlshell.properties")
          .addOption("h", "help"      , false, "Show this message")
          .addOption("u", "user"      , false, "User to connect on database")
          .addOption("p", "password"  , false, "Password to connect on database")
          .addOption("q", "query"     , true , "SQL Query to be executed")
          .addOption("l", "url"       , false, "JDBC Url to connect on database")
          .addOption("r", "driver"    , false, "JDBC driver to connect on database")
          .addOption("r", "render"    , true , "Define the output render for resultSet");
    }

    return gnuOptions;
  }

  public static CommandLine create(String... args) {
    CommandLineParser parser = new DefaultParser();
    Options options = constructGnuOptions();
    try {      
      return parser.parse(options, args);
    } catch (ParseException e) {
      e.printStackTrace();
      printUsage();
      return null;
    }
  }

  /**
   * Print usage information to provided OutputStream.
   * 
   * @param applicationName Name of application to list in usage.
   * @param options Command-line options to be part of usage.
   * @param out OutputStream to which to write the usage information.
   */
  public static void printUsage() {
    final PrintWriter writer = new PrintWriter(System.out);
    final HelpFormatter usageFormatter = new HelpFormatter();
    String header = "";
    String footer = "";
    usageFormatter.printHelp("sqlshell", header, constructGnuOptions(), footer, true);
    writer.close();
  }
}
