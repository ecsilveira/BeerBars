package com.beerbars.exception;

/**
 * Exce��o para Documento n�o Encontrado
 * @author B35579
 *
 */
public class DocumentNotFoundException extends Exception{

     private static final long serialVersionUID = 1L;

     /**
      *  Construtor Padrao.
      */
     public DocumentNotFoundException(){
         super("Documento n�o encontrado.");
     }
}
