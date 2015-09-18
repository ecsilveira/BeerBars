package com.beerbars.service;

import java.util.HashMap;
import java.util.List;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

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
        OSQLSynchQuery<ODocument> sql = new OSQLSynchQuery<ODocument>("select from OUser");
        sql.setFetchPlan("*:3");
        
        return getDao().getWithParam(sql, new HashMap<String, Object>());
    }

    @Override
    public <T> void get() {
        
    }
}
