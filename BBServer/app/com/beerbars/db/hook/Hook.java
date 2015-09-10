package com.beerbars.db.hook;

import com.orientechnologies.orient.core.hook.ODocumentHookAbstract;

/**
 * Hooks s�o como triggers de banco de dados.
 * Em Java, no OrientDB, s�o mais r�pidas que no pr�prio banco
 * 
 * @author B35579
 *
 */
public abstract class Hook extends ODocumentHookAbstract {
    
    /**
     * Retorna o Nome do Hook
     * @return
     */
    public abstract String getHookName();
    
    @Override
    public DISTRIBUTED_EXECUTION_MODE getDistributedExecutionMode() {
        return null;
    }

}
