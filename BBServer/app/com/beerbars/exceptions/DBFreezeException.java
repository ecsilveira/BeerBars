package com.beerbars.exceptions;

/**
 * Exception quando o banco de dados está em estado de freeze.
 * Seja para importação, export, backup e outros.
 * Impede que conexôes sejam abertas e ele perca sua consistencia.
 * 
 * @author B35579
 *
 */
public class DBFreezeException extends RuntimeException{
  
    private static final long serialVersionUID = 1L;

    /**
     *  Construtor Padrao.
     */
    public DBFreezeException(){
        super("Banco de Dados no estado de FREEZE!");
    }

}
