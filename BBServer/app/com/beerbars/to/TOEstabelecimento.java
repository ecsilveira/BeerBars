package com.beerbars.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author B35579
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TOEstabelecimento extends TOBase {
    
    @JsonProperty("nome_estabelecimento")
    private String nomeEstabelecimento;
    
    @JsonProperty("cidade")
    private String cidade;
    
    @JsonProperty("locLat")
    private Long localizationLatitute;
    
    @JsonProperty("locLon")
    private Long localizationLongitude;
    
    @JsonProperty("usuario")
    private TOUsuario usuario;
    
    /**
     *  Construtor Padrao.
     */
    public TOEstabelecimento(){
        super();
    }
    
    /**
     * @param data Construtor Padrao.
     */
    public TOEstabelecimento(JsonNode data){
        super(data);
        populaObjeto(this, data);        
    }

    /**
     * Get do atributo nomeEstabelecimento.
     * @return o atributo nomeEstabelecimento.
     */
    public String getNomeEstabelecimento() {
        return this.nomeEstabelecimento;
    }

    /**
     * Set do atributo nomeEstabelecimento.
     * @param nomeEstabelecimento - conteudo a ser atribuido ao atributo nomeEstabelecimento.
     */
    public void setNomeEstabelecimento(String nomeEstabelecimento) {
        this.nomeEstabelecimento = nomeEstabelecimento;
    }

    /**
     * Get do atributo cidade.
     * @return o atributo cidade.
     */
    public String getCidade() {
        return this.cidade;
    }

    /**
     * Set do atributo cidade.
     * @param cidade - conteudo a ser atribuido ao atributo cidade.
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    /**
     * Get do atributo localizationLatitute.
     * @return o atributo localizationLatitute.
     */
    public Long getLocalizationLatitute() {
        return this.localizationLatitute;
    }

    /**
     * Set do atributo localizationLatitute.
     * @param localizationLatitute - conteudo a ser atribuido ao atributo localizationLatitute.
     */
    public void setLocalizationLatitute(Long localizationLatitute) {
        this.localizationLatitute = localizationLatitute;
    }

    /**
     * Get do atributo localizationLongitude.
     * @return o atributo localizationLongitude.
     */
    public Long getLocalizationLongitude() {
        return this.localizationLongitude;
    }

    /**
     * Set do atributo localizationLongitude.
     * @param localizationLongitude - conteudo a ser atribuido ao atributo localizationLongitude.
     */
    public void setLocalizationLongitude(Long localizationLongitude) {
        this.localizationLongitude = localizationLongitude;
    }

    /**
     * Get do atributo usuario.
     * @return o atributo usuario.
     */
    public TOUsuario getUsuario() {
        return this.usuario;
    }

    /**
     * Set do atributo usuario.
     * @param usuario - conteudo a ser atribuido ao atributo usuario.
     */
    public void setUsuario(TOUsuario usuario) {
        this.usuario = usuario;
    }
}
