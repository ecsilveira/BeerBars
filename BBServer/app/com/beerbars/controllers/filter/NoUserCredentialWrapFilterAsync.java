package com.beerbars.controllers.filter;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;

import com.beerbars.controllers.response.WrapResponse;
import com.beerbars.logging.ServerLogger;

/**
 * Filter para requisições sem usuario e senha
 * @author Chico
 *
 */
public class NoUserCredentialWrapFilterAsync extends Action.Simple {

    @Override
    public F.Promise<SimpleResult> call(Context contexto) throws Throwable {
        ServerLogger.debug("NoUserCredentialsWrapFilterAsync.call() - begin...");
        Context.current.set(contexto);

        ServerLogger.debug("NoUserCredentialsWrapFilterAsync.call() - Chamada para o recurso: " + Context.current().request());
        
        //executes the request
        F.Promise<SimpleResult> tempResult = delegate.call(contexto);

        WrapResponse wr = new WrapResponse();
        F.Promise<SimpleResult> result = wr.wrapAsync(contexto, tempResult);
                
        ServerLogger.debug("NoUserCredentialsWrapFilterAsync.call() - end");
        return result;
    }

}
