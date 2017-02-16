package com.oscar.clusterlob.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oscar.cluster.core.BaseConnection;
import com.oscar.cluster.util.NodeServerInfo;

public class DBUtil {
	public static Connection getConnection(DBInfo info){
		Connection con = null;
		try{
			Class.forName(info.getDbDriver());
			con = DriverManager.getConnection(info.getDbUrl(), info.getDbUser(), info.getDbPsw());
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return con;
	}
	
	public static void closeConn(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	
	public static List<Connection> getNodeServerConnections(BaseConnection conn, String table) throws SQLException {
		Set<Integer> nodes = getTargetNodes(conn, table, 2);//获取在线节点
		Map<Integer, NodeServerInfo> nodeServerInfos = conn.getNodeServerInfos();
		Iterator<Integer> iterator = nodeServerInfos.keySet().iterator();
		List<Connection> connections = new ArrayList<Connection>();
		while (iterator.hasNext()) {
			Integer node = (Integer) iterator.next();
			if (nodes.contains(node)) {//只向原始节点导入
				NodeServerInfo nodeServerInfo = nodeServerInfos.get(node);
				Connection connection = getConnFromNodeInfo(nodeServerInfo);
				if (connection != null) {
					connections.add(connection);
					System.out.println("创建节点：" + node + " 连接成功");
				}
			}
		}
		return connections;
	}
	
	private static Connection getConnFromNodeInfo(NodeServerInfo nodeServerInfo) {
		String port = nodeServerInfo.getPort();
		String dbName = nodeServerInfo.getDbName();
		String host = nodeServerInfo.getHost();
		String url = "jdbc:oscar://" + host + ":" + port + "/" + dbName;
		DBInfo dbInfo = new DBInfo();
		dbInfo.setDbDriver("com.oscar.Driver");
		dbInfo.setDbUrl(url);
		dbInfo.setDbPsw(nodeServerInfo.getPassword());
		dbInfo.setDbUser(nodeServerInfo.getUser());
		try {
			return getConnection(dbInfo);
		} catch (Exception e) {
			// 一个节点连接不上不要紧
		}
		return null;
	}

	/**
	 * 获取目标原始节点
	 * @param conn
	 * @param table
	 * @param mode
	 * @return
	 */
	public static Set<Integer> getTargetNodes(BaseConnection conn, String table, int mode ) {
		int[][] baseNodeReplicaInfo = null;
		int[][] tmp_baseNodeReplicaInfo = null;
		try {
			tmp_baseNodeReplicaInfo = conn.getReplicaNodeInfo(table, mode);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        //获取可用节点
        for (int i = 0; i < tmp_baseNodeReplicaInfo.length; i++) {
            if (tmp_baseNodeReplicaInfo[i][0] == -1) {
                baseNodeReplicaInfo = new int[i][2];
                System.arraycopy(tmp_baseNodeReplicaInfo, 0, baseNodeReplicaInfo, 0, i);
                break;
            } 
        }

		Set<Integer> nodesSet = new HashSet<Integer>();
		for (int i = 0; i < baseNodeReplicaInfo.length; i++) {
			int originalNodeId = baseNodeReplicaInfo[i][0];
			System.out.println("oriNodeId:  " + originalNodeId);
			nodesSet.add(originalNodeId);
		}
		return nodesSet;
	}
	
}
