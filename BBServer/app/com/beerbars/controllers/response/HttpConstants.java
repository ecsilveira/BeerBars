package com.beerbars.controllers.response;

/**
 * HTTP Constants
 * 
 * @author B35579
 * 
 */
public interface HttpConstants {

    /**
     * HTTP Protocol
     * 
     * @author B35579
     * 
     */
    public interface HttpProtocol {
        /** HTTP 1.0 */
        String HTTP_1_0 = "HTTP/1.0";

        /** HTTP 1.1 */
        String HTTP_1_1 = "HTTP/1.1";
     
        /** CHUNKED */
        String CHUNKED = "chunked";
    }

    /**
     * HTTP Header
     * 
     * @author B35579
     * 
     */
    public interface Headers {
        /** TRANSFER ENCONDING */
        String TRANSFER_ENCODING = "Transfer-Encoding";
    }

}
