package com.beerbars.db;

/**
 * Enumeration com os ROLES de Usuários
 * @author B35579
 *
 */
public enum DatabaseRolesEnum {
    
    /** Usuario Comum */
    USUARIO_COMUM ("usuario"),
    /** Gerente Estabelecimento */
    GERENTE_ESTABELECIMENTO ("gerente_estabelecimento");
    
    private String name; 
    
    private DatabaseRolesEnum(String name){
        this.name = name;
    }
    
    /**
     * Retorna o nome da ROLE
     * @return
     */
    public String getRoleName(){
        return this.name;
    }
}
