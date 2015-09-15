package com.beerbars.db.hook;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;

import play.Logger;

import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.hook.ORecordHook;
import com.orientechnologies.orient.core.hook.ORecordHook.HOOK_POSITION;

/**
 * Classe responsavel por gerenciar as Hooks da conexao com o banco de dados
 *
 * @author Chico
 *
 */
public class HookManager {

    /**
     * Registra todas as Hooks basicas para a conexoes
     * @param db
     */
    public static void registerAll(ODatabaseDocument db) {
        Logger.debug("HookManager.registerAll - begin...");

        // checa se as hooks j� estao registradas para essa conexao
        boolean bAlreadyRegistred = false;

        Map<ORecordHook, HOOK_POSITION> hooks = db.getHooks();
        Iterator<ORecordHook> it = hooks.keySet().iterator();

        while (it.hasNext()) {
            if (it.next() instanceof Hook) {
                Logger.debug("Hooks ja resgistradas para essa conexao. Setando bAlreadyRegistred = true ");
                bAlreadyRegistred = true;
                break;
            }
        }
        
        //nao estao registradas
        if (!bAlreadyRegistred) {
            Logger.info("HookManager.registerAll - Registrando Hooks para a conexao");
            //db.registerHook(Audit.getIstance(), HOOK_POSITION.REGULAR);
            db.registerHook(new PasswordHook(), HOOK_POSITION.LAST);
            Logger.info("HookManager.registerAll - Registro Efetuado com Sucesso!");
        }
        Logger.debug("HookManager.registerAll - Hooks da Conexao: " + db.getHooks());

        Logger.debug("HookManager.registerAll - end");

    }
    
    /**
     * Desregistra todas as Hooks da conexao
     * @param db
     */
    @SuppressWarnings("unchecked")
    public static void unregisteredAll(ODatabaseDocumentTx db){
        Logger.debug("HookManager.unregisteredAll - begin...");
                
        Map<ORecordHook, HOOK_POSITION> hooks = db.getHooks();
        List<ORecordHook> hs = IteratorUtils.toList(hooks.keySet().iterator());

        for (ORecordHook hook : hs) {
            if (hook instanceof Hook) {
                Logger.debug("Desregistrando Hook: " + ((Hook)hook).getHookName() + " da conexao");
                db.unregisterHook(hook);
            }
        }
                
        Logger.debug("HookManager.unregisteredAll - end");
    }
    
}
