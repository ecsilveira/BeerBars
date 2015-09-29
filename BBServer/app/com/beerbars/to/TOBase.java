package com.beerbars.to;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.orientechnologies.orient.core.record.impl.ODocument;

/**
 * Classe Abstrata dos TOs
 * @author B35579
 *
 */
public abstract class TOBase {

    static ObjectMapper mapper = new ObjectMapper();
    JsonNode fullData;
    
    /**
     *  Construtor Padrao.
     */
    public TOBase(){
        this.fullData = mapper.createObjectNode();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * Construtor Padrao com dados a serem Armazenados
     * @param data
     */
    public TOBase(JsonNode data) {
        this();
        this.fullData = data;
    }

    /**
     * Retorna o ODocument com os dados do JSON
     * @return ODocument
     */
    public ODocument toODocument(){
        ODocument documento = new ODocument();
        return documento.fromJSON(getMergedData().toString());
    }
    
    private JsonNode getMergedData(){
        JsonNode classData = mapper.convertValue(this, JsonNode.class);
        JsonNode mergedData = merge(fullData, classData);
        return mergedData;
    }
    
    @Override
    public String toString(){
        return getMergedData().toString();
    }
    
    /**
     * Get do atributo mapper.
     * @return o atributo mapper.
     */
    public static ObjectMapper getMapper() {
        return mapper;
    }

    private static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();

        while (fieldNames.hasNext()) {
            String updatedFieldName = fieldNames.next();
            JsonNode valueToBeUpdated = mainNode.get(updatedFieldName);
            JsonNode updatedValue = updateNode.get(updatedFieldName);

            // If the node is an @ArrayNode
            if (valueToBeUpdated != null && updatedValue.isArray()) {
                // running a loop for all elements of the updated ArrayNode
                for (int i = 0; i < updatedValue.size(); i++) {
                    JsonNode updatedChildNode = updatedValue.get(i);
                    // Create a new Node in the node that should be updated, if there was no corresponding node in it
                    // Use-case - where the updateNode will have a new element in its Array
                    if (valueToBeUpdated.size() <= i) {
                        ((ArrayNode) valueToBeUpdated).add(updatedChildNode);
                    }
                    // getting reference for the node to be updated
                    JsonNode childNodeToBeUpdated = valueToBeUpdated.get(i);
                    merge(childNodeToBeUpdated, updatedChildNode);
                }
            // if the Node is an @ObjectNode
            } else if (valueToBeUpdated != null && valueToBeUpdated.isObject()) {
                merge(valueToBeUpdated, updatedValue);
            } else {
                if (mainNode instanceof ObjectNode) {
                    ((ObjectNode) mainNode).replace(updatedFieldName, updatedValue);
                }
            }
        }

        return mainNode;
    }
    
    @SuppressWarnings("unchecked")
    protected <T> void populaObjeto(T to, JsonNode data){
        try {
            T novoDado = (T) getMapper().treeToValue(data, to.getClass());
           
            for (Method metodo : novoDado.getClass().getDeclaredMethods()) {
                if (metodo.getName().startsWith("get")){
                    for (Field field : to.getClass().getDeclaredFields()) {
                        if (field.getName().equalsIgnoreCase(metodo.getName().substring(3))){
                            Method destino = to.getClass().getDeclaredMethod(metodo.getName().replaceFirst("g", "s"), metodo.getReturnType());  
                            destino.invoke(to, metodo.invoke(novoDado, new Object[]{}));  
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }    
}
