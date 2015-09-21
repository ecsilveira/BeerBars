package com.beerbars.exception;

/**
 * Exception para Documentos que não pertencem a Classe especificada
 * @author B35579
 *
 */
public class InvalidClassException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    /**
     *  Construtor Padrao.
     */
    public InvalidClassException(){
        super("Documento não pertence a classe especificada.");
    }
    
    /**
     * Construtor com Mensagem
     * @param message
     */
    public InvalidClassException(String message){
        super(message);
    }


}
