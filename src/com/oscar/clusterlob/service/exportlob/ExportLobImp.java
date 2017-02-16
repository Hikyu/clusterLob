package com.oscar.clusterlob.service.exportlob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.oscar.clusterlob.db.DBInfo;
import com.oscar.clusterlob.service.AbstractLobService;
import com.oscar.clusterlob.service.IOUtil;
import com.oscar.clusterlob.service.LobServiceInfo;
import com.oscar.jdbc.OscarJdbc2Connection;

public class ExportLobImp extends AbstractLobService implements ExportLob{
	private int defaultSize = 2 * 1024 * 1024;
	public ExportLobImp(DBInfo info) {
		super(info);
	}

	public boolean exportLob(LobServiceInfo importInfo) {
		List<OscarJdbc2Connection> nodeConnections = getNodeConnections(importInfo.getTableName());
		boolean success = false;
		for (Connection conn : nodeConnections) {
			if (executeExport(conn, importInfo)) {
				success = true;
				break;
			}
		}
		return success;
	}

	private boolean executeExport(Connection conn, LobServiceInfo importInfo) {
		boolean success = false;
		OutputStream streamOut = null;
		try {
			streamOut = IOUtil.getFileOutputStream(importInfo.getFilePath());
			String query = "select " + importInfo.getColumnName() + " from " + importInfo.getTableName();
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			byte[] readBytes = new byte[defaultSize];
			while (rs.next()) {
				InputStream streamIn = rs.getBinaryStream(1);
				int readSize = -1;
				while ((readSize = streamIn.read(readBytes)) != -1) {
					streamOut.write(readBytes, 0, readSize);
				}
				streamOut.flush();
				success = true;//读取到数据就算成功
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return false;
		} finally {
			if (streamOut != null) {
				try {
					streamOut.close();
				} catch (IOException e) {
				}
			}
		}
		return success;
	}

}
