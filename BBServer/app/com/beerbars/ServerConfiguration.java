package com.beerbars;

import play.Configuration;
import play.Play;

/**
 * Classe que armazena as configuracoes que vem do arquivo
 * conf/appplication.conf
 * 
 * @author B35579
 *
 */
public class ServerConfiguration {

	private static Configuration configuration = Play.application().configuration();
	
	/**
	 * Retona a URL do Banco de Dados
	 * @return
	 */
	public static String getDatabaseURL(){
		return configuration.getString(ServerConfigurationEnum.DATABASE_URL.toString());
	}
	
	/**
	 * Retorna o Username do Banco de Dados
	 * @return
	 */
	public static String getDatabaseUsername(){
		return configuration.getString(ServerConfigurationEnum.DATABASE_USERNAME.toString());
	}
	
	/**
	 * Retorna o Password do Banco de Dados
	 * @return
	 */
	public static String getDatabasePassword(){
		return configuration.getString(ServerConfigurationEnum.DATABASE_PASSWORD.toString());
	}
	
	/**
     * Retorna o Password do Banco de Dados
     * @return
     */
    public static Integer getDatabasePoolCapacity(){
        return configuration.getInt(ServerConfigurationEnum.DATABASE_POOL_CAPACITY.toString());
    }
	
	/**
	 * Retorna se o Token Provider usa REDIS (in memory database)
	 * @return
	 */
	public static Boolean isRedisTokenProviderEnabled(){
	    return configuration.getBoolean(ServerConfigurationEnum.REDIS_TOKENPROVIDER_ENABLE.toString());
	}
	
	/**
	 * Retorna em Minutos o tempo de TimeOut do Session Token
	 * @return
	 */
	public static Integer getSessionTokenTimeout(){
	    return configuration.getInt(ServerConfigurationEnum.SESSION_TOKENS_TIMEOUT.toString());
	}
	
	/**
     * Retorna a versao da API do Servidor
     * @return
     */
    public static String getServerVersion(){
        return configuration.getString(ServerConfigurationEnum.SERVER_VERSION.toString());
    }
    
    /**
     * Retorna a versao do Banco de Dados do Servidor
     * @return
     */
    public static String getDatabaseVersion(){
        return configuration.getString(ServerConfigurationEnum.DATABASE_VERSION.toString());
    }
}
