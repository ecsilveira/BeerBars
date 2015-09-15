package com.beerbars.controllers.response;

import play.mvc.Results;
import play.mvc.Results.Status;

/**
 * Enum que contem os codigos e mensagem que serao enviados na resposta do HTTP
 * 
 * @author B35579
 */
public enum HttpMessagesEnum {

    /** ERRO GENERICO */
    ERRO_GENERICO(40001, 400, HttpMessageTypeEnum.ERROR);

    private HttpMessageTypeEnum type;
    private int serverCode;
    private int httpCode;

    private HttpMessagesEnum(int serverCode, int httpCode, HttpMessageTypeEnum type) {
        this.serverCode = serverCode;
        this.httpCode = httpCode;
        this.type = type;
    }

    /**
     * Retorna o Tipo da Mensagem
     * 
     * @return
     */
    public HttpMessageTypeEnum getType() {
        return this.type;
    }

    /**
     * Retorna o Codigo da Mensagem do SERVIDOR
     * 
     * @return
     */
    public int getServerCode() {
        return this.serverCode;
    }

    /**
     * Retorna o C�digo da Mensagem HTTP
     * 
     * @return
     */
    public int getHttpCode() {
        return this.httpCode;
    }

    /**
     * Retorna um Play.STATUS com o codigo da Mensagem do SERVER
     * 
     * @return
     */
    public Status getStatus() {
        return Results.status(this.serverCode);
    }
    
    /**
     * Retorna o HttpMessageEnum pelo Codigo do SERVIDOR
     * @param serverCode
     * @return
     */
    public static HttpMessagesEnum getFromServerCode(int serverCode){
        for (HttpMessagesEnum v: values()){
            if (v.getServerCode() == serverCode){
                return v;
            }
        }
        return null;
    }

    protected enum HttpMessageTypeEnum {
        ERROR, WARNING, OK;
    }
}
