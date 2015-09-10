package com.beerbars.security;

/**
 * Chaves que vao ser armazenadas no Session Token Provider
 * @author B35579
 *
 */
public enum SessionKeysEnum {

    /** TOKEN */
    TOKEN ("X-BB-SESSION"),
    
    /** USERNAME */
    USERNAME ("USERNAME"),
    
    /** PASSWORD */
    PASSWORD ("PASSWORD"),
    
    /** START_TIME */
    START_TIME ("START_TIME"),
    
    /** EXPIRE_TIME */
    EXPIRE_TIME ("EXPIRE_TIME");
    
    private String key; 
    
    private SessionKeysEnum(String key) { 
        this.key = key; 
    } 
    
    public String toString(){ 
        return key; 
    } 
}