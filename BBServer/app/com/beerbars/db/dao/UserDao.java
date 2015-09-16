package com.beerbars.db.dao;

import com.beerbars.db.DatabaseManager;
import com.beerbars.db.DatabaseRolesEnum;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.metadata.security.ORole;

/**
 * DAO que controla os Usuarios
 * Trabalha com as Classes OUser e Usuarios no banco
 * @author B35579
 *
 */
public class UserDao {
    
    private ODatabaseDocument db;
    
    /**
     *  Construtor Padrao.
     */
    public UserDao(){
        db = DatabaseManager.getConnection();        
    }
    
    /**
     * Cria um usuario de banco de dados
     * @param username
     * @param password
     * @param role
     */
    public void create(String username, String password, DatabaseRolesEnum role){
        ORole dbRole = db.getMetadata().getSecurity().getRole(role.getRoleName());
        db.getMetadata().getSecurity().createUser(username, password, dbRole);
    }

}
