package com.beerbars.db;

import com.beerbars.Global;
import com.beerbars.ServerConfiguration;
import com.beerbars.db.hook.HookManager;
import com.beerbars.exception.DBFreezeException;
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
public class DatabaseManager {

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
     * Seta o banco de dados para o estado de Freeze Para acoes de Backup, Import e outras Manutencoes
     * 
     * @param bdFreeze
     */
    public static void setBdFreeze(Boolean bdFreeze) {
        DatabaseManager.bdFreeze = bdFreeze;
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
        ServerLogger.debug("DatabaseManager.open - begin...");

        ODatabaseDocument db = null;
        if (bdFreeze) {
            throw new DBFreezeException();
        }

        try {
            ServerLogger.debug("DatabaseManager.open - abrindo conexao com banco de dados");

            OPartitionedDatabasePoolFactory dbPoolFactory = Global.getPoolDatabaseFactory();
            db = dbPoolFactory.get(ServerConfiguration.getDatabaseURL(), username, password).acquire();

            ServerLogger.debug("DatabaseManager.open - abriu conexao para usuario: " + username);
            // TODO isso e necessario?????
            // DatabaseManager.username.set(username);
            // DatabaseManager.password.set(password);

            HookManager.registerAll(getConnection());

            ServerLogger.debug("DatabaseManager.open - return");
        } catch (Exception e) {
            ServerLogger.error("DatabaseManager.open - erro ao abrir conexao com banco de dados\n" + e.getMessage());
        }

        return db;
    }

    /**
     * Retorna a conexao da thread local para uso
     * @return
     */
    public static ODatabaseDocument getConnection() {
        ServerLogger.debug("DatabaseManager.getConnetion - begin...");
        ODatabaseDocumentTx db = null;
        try {
            db = (ODatabaseDocumentTx) ODatabaseRecordThreadLocal.INSTANCE.get();
            ServerLogger.info("DatabaseManager.getConnetion - dados da Conexao do Banco = ID: " + db + " " + ((Object) db).hashCode());
        } catch (ODatabaseException e) {
            ServerLogger.error("DatabaseManager.getConnetion - nao foi possivel obter a conexao na thread local", e);
        }

        ServerLogger.debug("DatabaseManager.getConnetion - return");
        return db;
    }

    /**
     * Faz uma chamada ao banco de dados para iniciar uma TRANSACTION
     */
    public static void requestTransaction() {
        ServerLogger.debug("DatabaseManager.requestTransaction - begin...");
        ServerLogger.debug("DatabaseManager.requestTransaction - contagem de transacoes -antes-: " + transactionCount.get());

        ODatabaseDocument db = getConnection();
        if (!isInTransaction(db)) {
            ServerLogger.debug("DatabaseManager.requestTransaction - comecou uma transacao");
            db.begin();
        }
        transactionCount.set(transactionCount.get().intValue() + 1);

        ServerLogger.debug("DatabaseManager.requestTransaction - contagem de transacoes -depois-: " + transactionCount.get());
    }

    private static boolean isInTransaction(ODatabaseDocument db) {
        return db.getTransaction().isActive();
    }
    
    /**
     * Faz uma chamada ao banco de dados para COMITAR uma transaction
     */
    public static void commitTransaction(){
        ServerLogger.debug("DatabaseManager.commitTransaction - begin...");
        ServerLogger.debug("DatabaseManager.commitTransaction - contagem de transacoes -antes-: " + transactionCount.get());
        
        ODatabaseDocument db = getConnection();
        if (isInTransaction(db)) {

            transactionCount.set(transactionCount.get().intValue() - 1);
            if (transactionCount.get() < 0){
                throw new RuntimeException("Commit sem transacao ativa");
            }
            if (transactionCount.get() >= 0) {
                db.commit();
                db.getTransaction().close();
                ServerLogger.debug("DatabaseManager.commitTransaction - commit efeutado!");
            }
        } else{
            throw new RuntimeException("Commit sem transacao ativa");
        }
        
        ServerLogger.debug("DatabaseManager.commitTransaction - contagem de transacoes -depois-: " + transactionCount.get());
    }
    
    /**
     * Faz uma chamada ao banco de dados para efetuar ROLLBACK em uma transaction
     */
    public static void rollbackTransaction() {
        ServerLogger.debug("DatabaseManager.rollbackTransaction - begin...");
        ServerLogger.debug("DatabaseManager.rollbackTransaction - contagem de transacoes -antes-: " + transactionCount.get());
        
        ODatabaseDocument db = getConnection();
        if (isInTransaction(db)) {
            db.getTransaction().rollback();
            db.getTransaction().close();
            transactionCount.set(0);
            ServerLogger.debug("DatabaseManager.rollbackTransaction - rollback efeutado!");
        }
        
        ServerLogger.debug("DatabaseManager.rollbackTransaction - contagem de transacoes -depois-: " + transactionCount.get());
    }

}
