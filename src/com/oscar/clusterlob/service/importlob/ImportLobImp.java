package com.oscar.clusterlob.service.importlob;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.util.List;

import com.oscar.clusterlob.db.DBInfo;
import com.oscar.clusterlob.service.AbstractLobService;
import com.oscar.clusterlob.service.IOUtil;
import com.oscar.clusterlob.service.LobServiceInfo;
import com.oscar.core.ImportHandler;
import com.oscar.jdbc.OscarJdbc2Connection;

public class ImportLobImp extends AbstractLobService implements ImportLob {
	
	public ImportLobImp(DBInfo info) {
		super(info);
	}
	
	public boolean importLob(LobServiceInfo importInfo) {
		List<OscarJdbc2Connection> nodeConnections = getNodeConnections(importInfo.getTableName());
		boolean success = false;
		for (com.oscar.core.BaseConnection conn : nodeConnections) {//尝试向所有节点导入
			if (executeImport(conn, importInfo)) {//有一个节点导入就算成功
				success = true;
			}
		}
		return success;
	}
	
	private boolean executeImport(com.oscar.core.BaseConnection conn, LobServiceInfo importInfo) {
		InputStream stream = null;
		try {
			stream = IOUtil.getFileInputStream(importInfo.getFilePath());
			/** ImportHandler不支持10m以上的列数据导入*/
//			ImportHandler handler = conn.createImportHandler(importInfo.getTableName());
//			handler.setColumnOrder(importInfo.getColumnName());
//			handler.setBinaryStream(1, stream);
//			handler.endRow();
//			handler.execute();
			OscarJdbc2Connection connection = (OscarJdbc2Connection)conn;
			String sql = "insert into " + importInfo.getTableName() + "(" + importInfo.getColumnName() + ")" + " values(?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			//负数则读取该流直到末尾    
			//int最大表示2G的数目，jdk1.5中，第三个参数为int,1.7为long
			ps.setBinaryStream(1, stream, -1);
			ps.execute();
		} catch (Exception e) {
			System.err.println(e.getMessage());;
			return false;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}

}
