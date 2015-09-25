package com.beerbars.db.dao;

import java.util.List;
import java.util.Map;

import com.beerbars.db.DatabaseManager;
import com.beerbars.exception.DocumentNotFoundException;
import com.beerbars.exception.InvalidClassException;
import com.beerbars.logging.ServerLogger;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;

/**
 * Classe Generica de Acesso ao Banco de Dados
 * 
 * @author B35579
 * 
 */
public class GenericDao {
    
    private ODatabaseDocument db;
    private String classe;
    
    /**
     * Construtor Padrao.
     * 
     * @param classe
     *            (aka tabela)
     */
    public GenericDao(String classe) {
        this.classe = classe;
        this.db = DatabaseManager.getConnection();
    }

    /**
     * Salva um documetno
     * 
     * @param documento
     * @return
     * @throws InvalidClassException 
     */
    public void save(ODocument documento) throws InvalidClassException {
        if (!checaClasseDocumento(documento)) {
            throw new InvalidClassException("O Documento não pertence a classe: " + this.classe);
        }
        db.save(documento);
    }

    /**
     * @param documento
     * @throws InvalidClassException 
     */
    public void delete(ODocument documento) throws InvalidClassException {
        if (!checaClasseDocumento((ODocument) documento)) {
            throw new InvalidClassException("O documento não pertence a classe: " + this.classe);
        }
        ServerLogger.debug("GenericDao.delete - begin...");
        db.delete(documento.getIdentity());
        ServerLogger.debug("GenericDao.delete - end");
    }

    /**
     * @param rid
     * @throws InvalidClassException
     * @throws DocumentNotFoundException
     */
    public void delete(ORID rid) throws DocumentNotFoundException, InvalidClassException {
        Object doc = get(rid);
        delete((ODocument) doc);
    }

    /**
     * Busca todos os registros da Classe
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ODocument> getAll() {
        ServerLogger.debug("GenericDao.getAll - call...");
        return (List<ODocument>) db.browseClass(this.classe);

    }

    /**
     * Retorna um documento pelo RID
     * 
     * @param rid
     * @return
     * @throws InvalidClassException 
     * @throws DocumentNotFoundException
     */
    public ODocument get(ORID rid) throws InvalidClassException, DocumentNotFoundException {
        ServerLogger.debug("GenericDao.get - begin...");
        Object doc = db.load(rid);

        if (doc == null) {
            throw new DocumentNotFoundException();
        }
        if (!(doc instanceof ODocument)) {
            throw new IllegalArgumentException(rid + " não é um Documento válido");
        }

        if (!checaClasseDocumento((ODocument) doc)) {
            throw new InvalidClassException("O RID: " + rid + " não pertence a coleção da classe: " + this.classe);
        }

        ServerLogger.debug("GenericDao.get - end");
        return (ODocument) doc;
    }

    /**
     * @param query
     * @param params
     * @return
     */
    public List<ODocument> getWithParam(OSQLSynchQuery<ODocument> query, Map<String, Object> params) {
        return db.command(query).execute(params);
    }

    /**
     * Verifica se o documento existe no banco
     * 
     * @param document
     * @return
     * @throws InvalidClassException
     * @throws DocumentNotFoundException
     */
    public boolean exists(ODocument document) throws InvalidClassException, DocumentNotFoundException {
        return exists(document.getRecord().getIdentity());
    }

    /**
     * Verifica se RID existe no banco
     * 
     * @param rid
     * @return
     * @throws InvalidClassException
     * @throws DocumentNotFoundException
     */
    public boolean exists(ORID rid) throws InvalidClassException, DocumentNotFoundException {
        ODocument doc = get(rid);
        return (doc != null);
    }

    /**
     * Retorna a quantidade de documentos da classe
     * 
     * @return
     */
    public long getCount() {
        return db.countClass(this.classe);
    }

    /**
     * Verifica se o documento pertence a classe que foi instanciada
     * 
     * @param documento
     * @return
     */
    public boolean checaClasseDocumento(ODocument documento) {
        if (documento != null && !documento.getClassName().equalsIgnoreCase(this.classe)) {
            return false;
        } else {
            return true;
        }
    }

}
