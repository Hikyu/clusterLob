package com.oscar.clusterlob;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

import com.oscar.clusterlob.db.DBInfo;
import com.oscar.clusterlob.service.LobServiceInfo;
import com.oscar.clusterlob.service.exportlob.ExportLob;
import com.oscar.clusterlob.service.exportlob.ExportLobImp;
import com.oscar.clusterlob.service.importlob.ImportLob;
import com.oscar.clusterlob.service.importlob.ImportLobImp;

/**
 * 配置参数
 * 
 * @author yukai
 *
 */
public class ExecuteCmd {
	String host=null;String port=null;String psw=null; 
	String user=null;String table=null;String column=null;
	String file=null;String db=null;String type=null;
	
	static Properties properties;
	static{
		String userDir = System.getProperty("user.dir");
		properties = loadProperties(userDir + File.separator + "config.properties");
	}
	public void exec(CommandLine command) throws IOException {
		if (parse(command)) {
			DBInfo dbInfo = new DBInfo();
			dbInfo.setDbDriver("com.oscar.cluster.Driver");
			dbInfo.setDbPsw(psw);
			dbInfo.setDbUser(user);
			dbInfo.setDbUrl("jdbc:oscarcluster://" + host + ":" + port + "/" + db);
			
			LobServiceInfo lobServiceInfo = new LobServiceInfo();
			lobServiceInfo.setColumnName(column);
			lobServiceInfo.setFilePath(file);
			lobServiceInfo.setTableName(table);
			
			try {
				if (type.equals("export")) {
					ExportLob exportLob = new ExportLobImp(dbInfo);
					if (exportLob.exportLob(lobServiceInfo)) {
						System.out.println("success");
					}else {
						System.out.println("fail");
					}
				} else {
					ImportLob importLob = new ImportLobImp(dbInfo);
					if (importLob.importLob(lobServiceInfo)) {
						System.out.println("success");
					} else {
						System.out.println("fail");
					}
				}
			} catch (Exception e) {
				Throwable cause = e.getCause();
				if (cause != null) {
					System.err.println(cause.getMessage());
				} else {
					System.err.println(e.getMessage());
				}
			}
		}
		
	}

	private boolean parse(CommandLine command) {
		if (command.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("help", ConfigOPtions.options);
		}
		
		if (command.hasOption(ConfigOPtions.HOST)) {
			host = command.getOptionValue(ConfigOPtions.HOST);
		} else {
			host = properties.getProperty(ConfigOPtions.HOST);
			if (stringEmpty(host)) {
				System.err.println("miss host");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.PORT)) {
			port = command.getOptionValue(ConfigOPtions.PORT);
		} else {
			port = properties.getProperty(ConfigOPtions.PORT);
			if (stringEmpty(port)) {
				System.err.println("miss port");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.USER)) {
			user = command.getOptionValue(ConfigOPtions.USER);
		} else {
			user = properties.getProperty(ConfigOPtions.USER);
			if (stringEmpty(user)) {
				System.err.println("miss user");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.PSW)) {
			psw = command.getOptionValue(ConfigOPtions.PSW);
		} else {
			psw = properties.getProperty(ConfigOPtions.PSW);
			if (stringEmpty(psw)) {
				System.err.println("miss psw");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.TABLENAME)) {
			table = command.getOptionValue(ConfigOPtions.TABLENAME);
		} else {
			table = properties.getProperty(ConfigOPtions.TABLENAME);
			if (stringEmpty(table)) {
				System.err.println("miss table");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.COLUMNNAME)) {
			column = command.getOptionValue(ConfigOPtions.COLUMNNAME);
		} else {
			column = properties.getProperty(ConfigOPtions.COLUMNNAME);
			if (stringEmpty(column)) {
				System.err.println("miss column");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.DBNAME)) {
			db = command.getOptionValue(ConfigOPtions.DBNAME);
		} else {
			db = properties.getProperty(ConfigOPtions.DBNAME);
			if (stringEmpty(db)) {
				System.err.println("miss db");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.FILE)) {
			file = command.getOptionValue(ConfigOPtions.FILE);
		} else {
			file = properties.getProperty(ConfigOPtions.FILE);
			if (stringEmpty(file)) {
				System.err.println("miss file");
				return false;
			}
		}
		
		if (command.hasOption(ConfigOPtions.TYPE)) {
			type = command.getOptionValue(ConfigOPtions.TYPE);
		} else {
			type = properties.getProperty(ConfigOPtions.TYPE);
			if (stringEmpty(type)) {
				System.err.println("miss type");
				return false;
			}
		}
		
		System.out.println(host + " "+ port + " " + user + " "+ psw + " "+ db + " "+ file + " "+ table + " "+ column + " "+ type);
		return true;
	}

	public boolean stringEmpty(String key) {
		return host == null || "".equals(host);
	}
	
	private static Properties loadProperties(String filePath) {
		Properties properties = new Properties();
		InputStream stream =null;
		try {
			stream = new FileInputStream(filePath);
			properties.load(stream);
		} catch (Exception e) {
			
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
		return properties;
		
	}

	
}
