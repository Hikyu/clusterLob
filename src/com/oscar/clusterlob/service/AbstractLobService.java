package com.oscar.clusterlob.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.oscar.cluster.core.BaseConnection;
import com.oscar.clusterlob.db.DBInfo;
import com.oscar.clusterlob.db.DBUtil;
import com.oscar.jdbc.OscarJdbc2Connection;

public class AbstractLobService {

	protected Connection clusterConn;
	protected DBInfo dbInfo;
	protected List<OscarJdbc2Connection> nodeConns;

	public AbstractLobService(DBInfo info) {
		this.dbInfo = info;
		nodeConns = new ArrayList<OscarJdbc2Connection>();
	}

	protected List<OscarJdbc2Connection> getNodeConnections(String tableName) {
		clusterConn = DBUtil.getConnection(dbInfo);
		try {
			//关闭一致性策略
			String consistent = "alter table "+ tableName +" dml consistent off";
			Statement statement = clusterConn.createStatement();
			statement.execute(consistent);
			
			List<Connection> nodeServerConnections = DBUtil.getNodeServerConnections((BaseConnection) clusterConn, tableName);
			Iterator<Connection> iterator = nodeServerConnections.iterator();
			while (iterator.hasNext()) {
				OscarJdbc2Connection conn = (OscarJdbc2Connection) iterator.next();
				nodeConns.add(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		if (nodeConns.isEmpty()) {
			throw new RuntimeException("查询节点信息失败");
		}
		
		return nodeConns;
	}
	
	public void close() {
		for (OscarJdbc2Connection connection : nodeConns) {
			DBUtil.closeConn(connection);
		}
		DBUtil.closeConn(clusterConn);
	}
}
