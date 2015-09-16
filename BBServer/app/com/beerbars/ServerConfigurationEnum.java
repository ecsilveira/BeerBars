package com.beerbars;

/**
 * Propriedades do arquivo de configuracao
 * conf/application.conf
 * @author B35579
 *
 */
public enum ServerConfigurationEnum {
    
	/** DATABASE URL */
    DATABASE_URL("odb.url"), 
    
    /** DATABASE USERNAME */
    DATABASE_USERNAME("odb.user"), 
    
    /** DATABASE PASSWORD */
	DATABASE_PASSWORD("odb.password"),
	
	/** DATABASE POOL CONECTION CAPACITY */
    DATABASE_POOL_CAPACITY("odb.pool.capacity"),
    
	/** REDIS TOKEN PROVIDER ENABLED */
    REDIS_TOKENPROVIDER_ENABLE("redis.tokenprovider.enable"),
    
    /** SESSION TOKENS TIMEOUT */
    SESSION_TOKENS_TIMEOUT("session.token.timeout"),
    
    /** SERVER VERSION */
    SERVER_VERSION("server.version"),
    
    /** DATABASE VERSION */
    DATABASE_VERSION("odb.version");

	private String value;
	
	private ServerConfigurationEnum(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}
}
