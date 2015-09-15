package com.beerbars.exception;

/**
 * Exception quando o banco de dados esta em estado de freeze.
 * Seja para importacao, export, backup e outros.
 * Impede que conexoes sejam abertas e ele perca sua consistencia.
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
