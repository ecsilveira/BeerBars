package com.beerbars;

/**
 * Propriedades do arquivo de configuração
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
    
	/** REDIS TOKEN PROVIDER ENABLED */
    REDIS_TOKENPROVIDER_ENABLE("redis.tokenprovider.enable"),
    
    /** SESSION TOKENS TIMEOUT */
    SESSION_TOKENS_TIMEOUT("session.token.timeout");

	private String value;
	
	private ServerConfigurationEnum(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}
}
