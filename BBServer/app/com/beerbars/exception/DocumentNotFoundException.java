package com.beerbars.exception;

/**
 * Exceção para Documento não Encontrado
 * @author B35579
 *
 */
public class DocumentNotFoundException extends Exception{

     private static final long serialVersionUID = 1L;

     /**
      *  Construtor Padrao.
      */
     public DocumentNotFoundException(){
         super("Documento não encontrado.");
     }
}
