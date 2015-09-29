package com.beerbars.to;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author B35579
 *
 */
public class TOUsuario extends TOBase{
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("sobrenome")
    private String sobrenome;
    
    /**
     *  Construtor Padrao.
     */
    public TOUsuario(){
        super();
    }
    
    /**
     * Get do atributo nome.
     * @return o atributo nome.
     */
    public String getNome() {
        return this.nome;
    }
    /**
     * Set do atributo nome.
     * @param nome - conteudo a ser atribuido ao atributo nome.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    /**
     * Get do atributo sobrenome.
     * @return o atributo sobrenome.
     */
    public String getSobrenome() {
        return this.sobrenome;
    }
    
    /**
     * Set do atributo sobrenome.
     * @param sobrenome - conteudo a ser atribuido ao atributo sobrenome.
     */
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    
    
}
