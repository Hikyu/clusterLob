package com.oscar.clusterlob;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ConfigOPtions {
	public static Options options;
	public static final String PORT = "port";
	public static final String HOST = "host";
	public static final String USER = "user";
	public static final String PSW = "psw";
	public static final String HELP = "help";
	public static final String DBNAME = "db";
	public static final String TABLENAME = "table";
	public static final String COLUMNNAME = "column";
	public static final String FILE = "file";
	public static final String TYPE = "type";
	
	static {
		options = new Options();
		config();
	}

	private static Options config() {
		Option help = new Option(HELP, "print this message");
		Option port = Option.builder(PORT)
				.desc("specify the cluster port")
				.argName("port")
				.hasArg()
				.build();
		
		Option host = Option.builder(HOST)
				.desc("specify the cluster host")
				.argName("host")
				.hasArg()
				.build();
		Option user = Option.builder(USER)
				.desc("specify the cluster db user")
				.argName("user")
				.hasArg()
				.build();
		Option psw = Option.builder(PSW)
				.desc("specify the cluster db password")
				.argName("psw")
				.hasArg()
				.build();
		
		Option dbName = Option.builder(DBNAME)
				.desc("specify the cluster db")
				.argName("db")
				.hasArg()
				.build();
		
		Option tableName = Option.builder(TABLENAME)
				.desc("specify the table")
				.argName("table")
				.hasArg()
				.build();
		Option columnName = Option.builder(COLUMNNAME)
				.desc("specify the column")
				.argName("column")
				.hasArg()
				.build();
		
		Option file = Option.builder(FILE)
				.desc("specify the file path")
				.argName("file")
				.hasArg()
				.build();
		
		Option type = Option.builder(TYPE)
				.desc("specify the type")
				.argName("type")
				.hasArg()
				.build();
		
		options.addOption(help)
		       .addOption(port)
		       .addOption(host)
		       .addOption(psw)
		       .addOption(user)
		       .addOption(dbName)
	           .addOption(tableName)
	           .addOption(file)
	           .addOption(type)
	           .addOption(columnName);
		       
		return options;
	}
}
