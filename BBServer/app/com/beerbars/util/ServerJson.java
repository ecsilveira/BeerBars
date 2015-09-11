package com.beerbars.util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.json.MetricsModule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;

/**
 * Server JSon
 * @author B35579
 *
 */
public class ServerJson {
    
    private static final ObjectMapperExt MAPPER = new ObjectMapperExt().registerModule(new MetricsModule(TimeUnit.SECONDS,TimeUnit.SECONDS,false));

    /**
     * Mapper de JSon
     * @author B35579
     *
     */
    public static class ObjectMapperExt  extends ObjectMapper{

        private static final long serialVersionUID = 1L;

        /**
         * 
         * @param in
         * @return
         */
        public JsonNode readTreeOrMissing(String in){
            try {
                return readTree(in);
            } catch (IOException e) {
                return MissingNode.getInstance();
            }
        }

        @Override
        public ObjectMapperExt registerModule(Module module) {
            super.registerModule(module);
            return this;
        }
    }

    /**
     * Faz alguma coisa
     * @param value
     * @return
     */
    public static String prettyPrinted(JsonNode value){
        try {
            return mapper().writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
    
    /**
     * Retorna o Map de JSON
     * @return
     */
    public static ObjectMapperExt mapper(){
        return MAPPER;
    }
}
