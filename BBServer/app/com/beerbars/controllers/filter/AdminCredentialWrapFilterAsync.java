package com.beerbars.controllers.filter;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;

import com.beerbars.ServerConfiguration;
import com.beerbars.controllers.response.WrapResponse;
import com.beerbars.logging.ServerLogger;

/**
 * Filter para requisições com usuario e senha de ADMIN
 * @author Chico
 *
 */
public class AdminCredentialWrapFilterAsync extends Action.Simple {

    @Override
    public F.Promise<SimpleResult> call(Context contexto) throws Throwable {
        ServerLogger.debug("AdminCredentialWrapFilterAsync.call() - begin...");
        
        Context.current.set(contexto);

        //seta os dados do usuario admin para abrir conexao e criar um usuario no banco de dados
        ServerLogger.debug("AdminCredentialWrapFilterAsync.call() - setando usuario e senha ADMIN do banco para o contexto");
        contexto.args.put("username", ServerConfiguration.getDatabaseUsername());
        contexto.args.put("password", ServerConfiguration.getDatabasePassword());
        
        ServerLogger.debug("AdminCredentialWrapFilterAsync.call() - chamada para o recurso: " + Context.current().request());
        
        //executes the request
        F.Promise<SimpleResult> tempResult = delegate.call(contexto);

        WrapResponse wr = new WrapResponse();
        F.Promise<SimpleResult> result = wr.wrapAsync(contexto, tempResult);
                
        ServerLogger.debug("AdminCredentialWrapFilterAsync.call() - end");
        return result;
    }

}
