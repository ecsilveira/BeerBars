package com.beerbars.db;

import com.beerbars.ServerConfiguration;
import com.beerbars.logging.ServerLogger;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

/**
 * 
 * @author chico
 *
 */
public class DBHelper {
	
	private static ODatabaseDocumentTx open(String username, String password){
		ServerLogger.debug("abrindo conexao com banco de dados");
		ODatabaseDocumentTx conn = new ODatabaseDocumentTx(ServerConfiguration.getDatabaseURL()).open(username, password);
		return conn;
	}
	
	
//	private static ODatabaseDocumentTx open(String appcode, String username, String password)
//			throws InvalidAppCodeException {
//
//		if (appcode == null || !appcode.equals(BBConfiguration.configuration.getString(BBConfiguration.APP_CODE)))
//			throw new InvalidAppCodeException(
//					"Authentication info not valid or not provided: " + appcode + " is an Invalid App Code");
//		if (dbFreeze) {
//
//			throw new ShuttingDownDBException();
//		}
//		String databaseName = BBConfiguration.getDBDir();
//		if (BaasBoxLogger.isDebugEnabled())
//			BaasBoxLogger.debug("opening connection on db: " + databaseName + " for " + username);
//
//		ODatabaseDocumentPool odp = ODatabaseDocumentPool.global();
//		ODatabaseDocumentTxPooled conn = new ODatabaseDocumentTxPooled(odp, "plocal:" + BBConfiguration.getDBDir(),
//				username, password);
//
//		HooksManager.registerAll(getConnection());
//		DbHelper.appcode.set(appcode);
//		DbHelper.username.set(username);
//		DbHelper.password.set(password);
//
//		return getConnection();
//	}

}
