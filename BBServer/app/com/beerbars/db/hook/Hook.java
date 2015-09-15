package com.beerbars.db.hook;

import com.orientechnologies.orient.core.hook.ODocumentHookAbstract;

/**
 * Hooks sao como triggers de banco de dados.
 * Em Java, no OrientDB, sao mais rapidas que no proprio banco
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
