package com.oscar.clusterlob.service.importlob;

import com.oscar.clusterlob.service.LobServiceInfo;

public interface ImportLob {
	boolean importLob(LobServiceInfo importInfo);
	void close();
}
