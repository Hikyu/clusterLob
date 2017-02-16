package com.oscar.clusterlob;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.oscar.clusterlob.db.DBInfo;
import com.oscar.clusterlob.service.LobServiceInfo;
import com.oscar.clusterlob.service.importlob.ImportLob;
import com.oscar.clusterlob.service.importlob.ImportLobImp;

public class App {
	public static String path = "C:/Users/kyu/Desktop/1.txt";
	public static void main(String[] args) {
//		initData(11 * 1024 * 1024);
//		try {
//			DBInfo dbInfo = new DBInfo();
//			dbInfo.setDbDriver("com.oscar.cluster.Driver");
//			dbInfo.setDbUrl("jdbc:oscarcluster://192.168.101.24:2003/yk");
//			dbInfo.setDbPsw("dba");
//			dbInfo.setDbUser("dba");
//			ImportLob importLob = new ImportLobImp(dbInfo);
//			
//			LobServiceInfo importInfo = new LobServiceInfo();
//			importInfo.setColumnName("a");
//			importInfo.setFilePath(path);
//			importInfo.setTableName("yk1");
//			boolean b = importLob.importLob(importInfo);
//			System.out.println(b);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
		
		parseArgs(args);
	}
	
	public static void initData(int size) {
		try {
			char string[] = new char[size];
			for (int i = 0; i < size; i++) {
				string[i] = 'a';
			}
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
			stream.write(new String(string).getBytes());
			stream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void parseArgs(String[] args) {
		Options options = ConfigOPtions.options;
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println( "Parsing failed.  Reason: " + e.getMessage());
			return;
		}

		try {
			ExecuteCmd eCmd = new ExecuteCmd();
			eCmd.exec(cmd);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		
	}
}
