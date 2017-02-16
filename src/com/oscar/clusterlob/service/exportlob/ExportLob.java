package com.oscar.clusterlob.service.exportlob;

import com.oscar.clusterlob.service.LobServiceInfo;

public interface ExportLob {
	boolean exportLob(LobServiceInfo importInfo);
	void close();
}
