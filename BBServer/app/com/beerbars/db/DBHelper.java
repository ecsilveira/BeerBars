package com.beerbars.db;

import com.beerbars.ServerConfiguration;
import com.beerbars.db.hook.HookManager;
import com.beerbars.exceptions.DBFreezeException;
import com.beerbars.logging.ServerLogger;
import com.orientechnologies.orient.core.db.ODatabaseRecordThreadLocal;
import com.orientechnologies.orient.core.db.OPartitionedDatabasePoolFactory;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.exception.ODatabaseException;

/**
 * 
 * @author chico
 * 
 */
public class DBHelper {

    private static volatile Boolean bdFreeze = false;

    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        };
    };

    // private static ThreadLocal<String> username = new ThreadLocal<String>() {
    // protected String initialValue() {
    // return "";
    // };
    // };
    //
    // private static ThreadLocal<String> password = new ThreadLocal<String>() {
    // protected String initialValue() {
    // return "";
    // };
    // };

    /**
     * Seta o banco de dados para o estado de Freeze Para ações de Backup, Import e outras Manutençoes
     * 
     * @param bdFreeze
     */
    public static void setBdFreeze(Boolean bdFreeze) {
        DBHelper.bdFreeze = bdFreeze;
    }

    /**
     * Abre uma conexao com o banco de dados Normalmente utilizado no ConnectDBWrapper
     * 
     * @param username
     * @param password
     * @return ODatabaseDocument
     * @throws DBFreezeException
     */
    public static ODatabaseDocument openConnection(String username, String password) {
        return open(username, password);
    }

    private static ODatabaseDocument open(String username, String password) throws DBFreezeException {
        ServerLogger.debug("DBHelper.open - begin...");

        ODatabaseDocument db = null;
        if (bdFreeze) {
            throw new DBFreezeException();
        }

        try {
            ServerLogger.debug("DBHelper.open - abrindo conexao com banco de dados");

            OPartitionedDatabasePoolFactory dbPoolFactory = new OPartitionedDatabasePoolFactory();
            db = dbPoolFactory.get(ServerConfiguration.getDatabaseURL(), username, password).acquire();

            ServerLogger.debug("DBHelper.open - abriu conexao para usuario: " + username);
            // TODO isso é necessario?????
            // DBHelper.username.set(username);
            // DBHelper.password.set(password);

            HookManager.registerAll(getConnection());

            ServerLogger.debug("DBHelper.open - return");
        } catch (Exception e) {
            ServerLogger.error("DBHelper.open - erro ao abrir conexao com banco de dados\n" + e.getMessage());
        }

        return db;
    }

    private static ODatabaseDocument getConnection() {
        ServerLogger.debug("DBHelper.getConnetion - begin...");
        ODatabaseDocumentTx db = null;
        try {
            db = (ODatabaseDocumentTx) ODatabaseRecordThreadLocal.INSTANCE.get();
            ServerLogger.info("DBHelper.getConnetion - dados da Conexao do Banco = ID: " + db + " " + ((Object) db).hashCode());
        } catch (ODatabaseException e) {
            ServerLogger.error("DBHelper.getConnetion - nao foi possivel obter a conexao na thread local", e);
        }

        ServerLogger.debug("DBHelper.getConnetion - return");
        return db;
    }

    /**
     * 
     */
    public static void requestTransaction() {
        ServerLogger.debug("DBHelper.requestTransaction - begin...");
        ServerLogger.debug("DBHelper.requestTransaction - contagem de transacoes -antes-: " + transactionCount.get());

        ODatabaseDocument db = getConnection();
        if (!isInTransaction(db)) {
            ServerLogger.debug("DBHelper.requestTransaction - comecou uma transacao");
            db.begin();
        }
        transactionCount.set(transactionCount.get().intValue() + 1);

        ServerLogger.debug("DBHelper.requestTransaction - contagem de transacoes -depois-: " + transactionCount.get());
    }

    private static boolean isInTransaction(ODatabaseDocument db) {
        return db.getTransaction().isActive();
    }

}
