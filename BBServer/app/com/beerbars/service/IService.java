package com.beerbars.service;

import java.util.List;

import com.beerbars.exception.InvalidClassException;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Interface padrao dos Services
 * @author Chico
 *
 */
public interface IService {

    /**
     * Metodo basico para inserção 
     * @param documento 
     * @throws InvalidModelException 
     */
    public void insert(ODocument documento) throws InvalidClassException;
    
    /**
     * Metodo basico para excluisão
     * @param documento 
     * @throws InvalidModelException 
     */
    public void delete(ODocument documento) throws InvalidClassException;

    /**
     * Metodo basico para alterar
     * @param documento 
     * @throws InvalidModelException 
     */
    public void update(ODocument documento) throws InvalidClassException;
    
    /**
     * Metodo basico para listar
     * @return
     */
    public List<?> list();
    
    /**
     * Metodo basico para obter
     * @return 
     */
    public <T> void get();
    
}
