package com.beerbars.security;

import com.beerbars.ServerConfiguration;

/**
 * Factory que cria o Token Provider conforme os Tipos Disponiveis (TokenProviderEnum)
 * 
 * @author B35579
 * 
 */
public class SessionTokenProviderFactory {

    /**
     * Tipos de Token Providers
     * 
     * @author B35579
     * 
     */
    public enum TokenProviderEnum {
        /** DEFAULT PROVIDER */
        DEFAULT_PROVIDER,

        /** REDIS PROVIDER */
        REDIS_PROVIDER;
    }

    protected SessionTokenProviderFactory() {

    }

    protected static SessionTokenProviderInterface getSessionTokenProvider(TokenProviderEnum providerEnum) {

        switch (providerEnum) {
            case DEFAULT_PROVIDER:
                return SessionTokenProvider.getSessionTokenProvider();
            case REDIS_PROVIDER:
                // TODO futuramente ver a necessidade de trocar para o REDIS - mais segurança???
                throw new RuntimeException("SessionTokenProviderInterface.getSessionTokenProvider - Token Provider não implementado");
            default:
                throw new RuntimeException("SessionTokenProviderInterface.getSessionTokenProvider - Default not Found");
        }
    }

    /**
     * Retorna o Token Provider conforme as configuracoes do Server
     * 
     * @return
     */
    public static SessionTokenProviderInterface getSessionTokenProvider() {

        if (ServerConfiguration.isRedisTokenProviderEnabled()) {
            return getSessionTokenProvider(TokenProviderEnum.REDIS_PROVIDER);
        } else {
            return getSessionTokenProvider(TokenProviderEnum.DEFAULT_PROVIDER);
        }

    }

}
