package com.beerbars.controllers.filter;

import org.apache.commons.lang.StringUtils;

import play.mvc.Action;
import play.mvc.Http.Context;

import com.beerbars.logging.ServerLogger;
import com.beerbars.security.SessionKeysEnum;
import com.beerbars.security.SessionTokenProviderFactory;
import com.google.common.collect.ImmutableMap;

/**
 * Classe principal para filtros de requisicao que trabalham com Credenciais
 * 
 * Setam no Contexto o usuario e senha que obtiveram atraves da consulta no SessionProvider com o TOKEN enviado na requisição
 * 
 * @author B35579
 * 
 */
public abstract class CredentialWrapFilter extends Action.Simple {

    /**
     * Seta as credenciais de acesso ao banco, obtidas pelo TOKEN no SessionProvider
     * @param contexto
     * @return Boolean
     */
    public Boolean setCurrentContextCredential(Context contexto) {

        // injeta o usuario e senha que veio do contexto
        String token = contexto.request().getHeader(SessionKeysEnum.TOKEN.toString());
        if (StringUtils.isEmpty(token)) {
            token = contexto.request().getQueryString(SessionKeysEnum.TOKEN.toString());
        }

        if (token != null) {
            ServerLogger.debug("CredentialWrapFilter.setCurrentContextCredential() - TOKEN header recebido: " + token);
            ImmutableMap<SessionKeysEnum, ? extends Object> sessionData = SessionTokenProviderFactory.getSessionTokenProvider().getSession(token);
            if (sessionData != null) {
                ServerLogger.debug("CredentialWrapFilter.setCurrentContextCredential() - SESSION TOKEN encontrado");
                contexto.args.put("username", sessionData.get(SessionKeysEnum.USERNAME));
                contexto.args.put("password", sessionData.get(SessionKeysEnum.PASSWORD));
                contexto.args.put("token", token);

                ServerLogger.error("CredentialWrapFilter.setCurrentContextCredential() - username: "
                        + (String) sessionData.get(SessionKeysEnum.USERNAME));
                ServerLogger.error("CredentialWrapFilter.setCurrentContextCredential() - password: <hidden>");
                return true;
            } else {
                ServerLogger.error("CredentialWrapFilter.setCurrentContextCredential() - SESSION TOKEN não encontrado no SessionTokenProvider");
                return false;
            }
        } else {
            ServerLogger.error("CredentialWrapFilter.setCurrentContextCredential() - SESSION TOKEN header é nulo");
            return false;
        }
    }
}
