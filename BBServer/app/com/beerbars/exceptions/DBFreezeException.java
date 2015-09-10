package com.beerbars.exceptions;

/**
 * Exception quando o banco de dados est� em estado de freeze.
 * Seja para importa��o, export, backup e outros.
 * Impede que conex�es sejam abertas e ele perca sua consistencia.
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
