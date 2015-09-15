package com.beerbars.controllers.helper;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Http.Context;

/**
 * Classe auxiliar que monta as mensagem de resposta do Wrapper
 * 
 * @author B35579
 * 
 */
public class WrapResponseHelper {

    /**
     * Monta Mensgem OK
     * 
     * @param ctx
     * @return
     */
    public static String preludeOk(Context ctx) {
        ctx.request().getQueryString("call_id");
        return preludeOk(ctx.request().getQueryString("call_id"), !StringUtils.isEmpty(ctx.response().getHeaders().get("X-BB-MORE")), ctx.response()
                .getHeaders().get("X-BB-MORE"));
    }

    /**
     * Monta Mensagem OK
     * 
     * @param callId
     * @param setMoreField
     * @param moreFieldValue
     * @return
     */
    public static String preludeOk(String callId, boolean setMoreField, String moreFieldValue) {
        StringBuilder toReturn = new StringBuilder();
        return toReturn.append("\"result\":\"ok\",").append(setCallIdOnResult(callId)).append(setMoreField(setMoreField, moreFieldValue))
                .append("\"data\":").toString();
    }

    /**
     * Monta o Fim da Mensagem OK
     * @param statusCode
     * @return
     */
    public static String endOk(int statusCode) {
        StringBuilder toReturn = new StringBuilder();
        return toReturn.append(",\"http_code\":").append(statusCode).toString();
    }

    private static String setCallIdOnResult(String callId) {
        if (!StringUtils.isEmpty(callId)) {
            return new StringBuilder("\"call_id\":\"").append(callId.replace("\"", "\\\"") + "\",").toString();
        } else {
            return "";
        }
    }

    private static String setMoreField(boolean set, String value) {
        String toRet = "";
        if (set) {
            toRet = new StringBuilder("\"more\":").append(value).append(",").toString();
        }
        return toRet;
    }
}
