package com.beerbars.db.hook;

import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Hook para ocultar o "PASSWORD" quando se le um documento do tipo OUser
 * 
 * @author B35579
 * 
 */
public class PasswordHook extends Hook {

    /**
     * Construtor Padrao.
     */
    public PasswordHook() {
        super();
    }

    @Override
    public void onRecordAfterRead(ODocument doc) {
        if (doc instanceof ODocument) {
            if ("OUser".equalsIgnoreCase(((ODocument) doc).getClassName())) {
                ((ODocument) doc).removeField("password");
            }
        }
    }// onRecordAfterRead

    @Override
    public String getHookName() {
        return "PasswordHook";
    }

}