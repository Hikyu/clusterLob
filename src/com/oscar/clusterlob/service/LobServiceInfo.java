package com.oscar.clusterlob.service;

import com.oscar.cluster.util.MetaDataUtil;

public class LobServiceInfo {
	private String schemaName;
	private String tableName;
	private String columnName;
	private String filePath;

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = MetaDataUtil.toServerName(tableName);
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName.toUpperCase();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
