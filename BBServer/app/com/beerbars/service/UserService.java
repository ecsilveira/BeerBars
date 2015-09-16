package com.beerbars.service;

import java.util.List;

/**
 * Service responsavel pelos Usuarios
 * @author B35579
 *
 */
public class UserService extends BaseService{

    /**
     *  Construtor Padrao.
     */
    public UserService() {
        super("Usuarios");
    }

    @Override
    public List<?> list() {
        
        
        getDao().getWithParam(query, params)
    }

    @Override
    public <T> void get() {
    }
}
