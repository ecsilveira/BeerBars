package com.beerbars.controllers.filter;

import org.apache.commons.lang.StringUtils;

import com.beerbars.controllers.response.WrapResponse;
import com.beerbars.logging.ServerLogger;
import com.beerbars.security.SessionKeysEnum;

import play.libs.F;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;

/**
 * Filter para requisições sem usuario e senha
 * @author Chico
 *
 */
public class UserCredentialWrapFilterAsync extends CredentialWrapFilter {

    @Override
    public F.Promise<SimpleResult> call(Context contexto) throws Throwable {
        ServerLogger.debug("NoUserCredentialsWrapFilterAsync.call() - begin...");
        
        F.Promise<SimpleResult> tempResult = null;
        Context.current.set(contexto);

        //seta os dados do usuario admin para abrir conexao e criar um usuario no banco de dados
        String token = contexto.request().getHeader(SessionKeysEnum.TOKEN.toString());
        if (StringUtils.isEmpty(token)){
            token = contexto.request().getQueryString(SessionKeysEnum.TOKEN.toString());
        }
        
        //TODO ver se se é necessario usar authorization no header?
        //String authHeader = contexto.request().getHeader("authorization");

        if (StringUtils.isEmpty(token)) {
            tempResult = F.Promise.<SimpleResult>pure(unauthorized("Session Token não encontrado na requisição."));
        }else{
           if (!setCurrentContextCredential(contexto)){
              tempResult = F.Promise.<SimpleResult>pure(unauthorized("Session Token não encontrado na sessão do servidor."));
           }
        }
   
        
        ServerLogger.debug("NoUserCredentialsWrapFilterAsync.call() - Chamada para o recurso: " + Context.current().request());
        
        //executa a chamada, se nao tem um resultado já engatilhado
        if (tempResult == null){
            tempResult = delegate.call(contexto);
        }

        WrapResponse wr = new WrapResponse();
        F.Promise<SimpleResult> result = wr.wrapAsync(contexto, tempResult);
                
        ServerLogger.debug("NoUserCredentialsWrapFilterAsync.call() - end");
        return result;
    }

}
