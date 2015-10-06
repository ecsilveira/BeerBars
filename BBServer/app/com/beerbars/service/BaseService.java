package com.beerbars.service;

import com.beerbars.db.dao.GenericDao;
import com.beerbars.exception.InvalidClassException;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Classe Service que tem os métodos básicos
 * @author B35579
 *
 */
public abstract class BaseService implements IService{
    
    private String tabela;
    private GenericDao dao;
    
    /**
     * Construtor Padrao.
     * @param tabela 
     */
    public BaseService(String tabela){
        this.tabela = tabela;
        this.dao = new GenericDao(this.tabela);
    }
    
    /**
     * Construtor Padrao.
     * @param classe 
     */
    public BaseService(String classe, String dados){
        this.tabela = classe;
        this.dao = new GenericDao(this.tabela);
    }
    
    /**
     * Retorna o DAO do Service
     * @return
     */
    public GenericDao getDao(){
        return dao;
    }    
    
    public void insert(ODocument documento) throws InvalidClassException {
        dao.save(documento);
    }

    public void delete(ODocument documento) throws InvalidClassException {
        dao.delete(documento);
    }

    public void update(ODocument documento) throws InvalidClassException {
        dao.save(documento);
    }
}
