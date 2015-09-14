package com.beerbars.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.exception.ExceptionUtils;

import play.libs.F;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.RequestBody;
import play.mvc.Result;

import com.baasbox.BBConfiguration;
import com.baasbox.db.DbHelper;
import com.baasbox.exception.InvalidAppCodeException;
import com.baasbox.security.SessionKeys;
import com.baasbox.service.logging.BaasBoxLogger;
import com.baasbox.service.user.UserService;
import com.beerbars.security.SessionTokenProviderFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.orientechnologies.orient.core.exception.OSecurityAccessException;

/**
 * Classe Controller reponsaveis pelo acesso dos Usuarios tanto para Login, Cadastro, Login via Plugins
 * 
 * @author B35579
 * 
 */
public class Credentials extends Controller {

    /**
     * Metodo Responsavel pelo Login do Usuario
     * @return F.Promise<Result>
     */
    //@With ({NoUserCredentialWrapFilterAsync.class})
    public static Promise<Status> login(){
        
        final String username;
        final String password;
        final String loginData;
        
        RequestBody body = request().body();
        //BaasBoxLogger.debug ("Login called. The body is: {}", body);
        if (body==null){
            return F.Promise.pure(badRequest("missing data: is the body x-www-form-urlencoded or application/json? Detected: " + request().getHeader(CONTENT_TYPE)));
        }
        
        Map<String, String[]> bodyUrlEncoded = body.asFormUrlEncoded();
        if (bodyUrlEncoded!=null){
            if(bodyUrlEncoded.get("username")==null) 
                return F.Promise.pure(badRequest("The 'username' field is missing"));
            else username=bodyUrlEncoded.get("username")[0];
            if(bodyUrlEncoded.get("password")==null) {
                return F.Promise.pure(badRequest("The 'password' field is missing"));
            }
            else{ password=bodyUrlEncoded.get("password")[0];
            }
            
            
            if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("Username " + username);
            if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("Password " + password);
            
            if (username.equalsIgnoreCase(BBConfiguration.getBaasBoxAdminUsername())
                    ||
                    username.equalsIgnoreCase(BBConfiguration.getBaasBoxUsername())
                    ) return F.Promise.pure(forbidden(username + " cannot login"));
    
            if (bodyUrlEncoded.get("login_data")!=null)
                loginData=bodyUrlEncoded.get("login_data")[0];
            else loginData=null;
            if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("LoginData" + loginData);
        }else{
            
            JsonNode bodyJson = body.asJson();

            if (bodyJson==null) {
                return F.Promise.pure(badRequest("missing data : is the body x-www-form-urlencoded or application/json? Detected: " + request().getHeader(CONTENT_TYPE)));
            }
            
            if(bodyJson.get("username")==null){ 
                return F.Promise.pure(badRequest("The 'username' field is missing"));
            }
            else{ 
                username=bodyJson.get("username").asText();
            }
            
            if(bodyJson.get("password")==null){ 
                return F.Promise.pure(badRequest("The 'password' field is missing"));
            }
            else{ 
                password=bodyJson.get("password").asText();
            }
            
            
            
            if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("Username " + username);
            if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("Password " + password);
            
            if (username.equalsIgnoreCase(BBConfiguration.getBaasBoxAdminUsername())
                    ||
                    username.equalsIgnoreCase(BBConfiguration.getBaasBoxUsername())
                    ) return F.Promise.pure(forbidden(username + " cannot login"));
    
            if (bodyJson.get("login_data")!=null){
                loginData=bodyJson.get("login_data").asText();
            }
            
            else{
                loginData=null;
            }
            if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("LoginData" + loginData);   
        }

        return F.Promise.promise(()->{
            String user;
            //the filter does not inject the appcode. Actually the login endpoint is the only one that does not enforce a check on the appcode presence. 
            //This is not correct, BTW, for the moment we just patch it

            Http.Context.current().args.put("appcode", appcode);
            try (ODatabaseRecordTx db = DbHelper.open(appcode,username,password)){
                user = prepareResponseToJson(UserService.getCurrentUser());
                if (loginData != null) {
                    JsonNode loginInfo = null;
                    try {
                        loginInfo =Json.parse(loginData);
                    } catch (Exception e){
                        if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug ("Error parsong login_data field");
                        if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug (ExceptionUtils.getFullStackTrace(e));
                        return badRequest("login_data field is not a valid json string");
                    }

                    Iterator<Entry<String, JsonNode>> it =loginInfo.fields();
                    HashMap<String, Object> data = new HashMap<String, Object>();
                    while (it.hasNext()){
                        Entry<String, JsonNode> element = it.next();
                        String key=element.getKey();
                        Object value=element.getValue().asText();
                        data.put(key,value);
                    }
                    UserService.registerDevice(data);
                }
                ImmutableMap<SessionKeys, ? extends Object> sessionObject = SessionTokenProviderFactory.getSessionTokenProvider().setSession(appcode, username, password);
                response().setHeader(SessionKeys.TOKEN.toString(), (String) sessionObject.get(SessionKeys.TOKEN));

                ObjectMapper mapper = BBJson.mapper();
                user = user.substring(0,user.lastIndexOf("}")) + ",\""+SessionKeys.TOKEN.toString()+"\":\""+ (String) sessionObject.get(SessionKeys.TOKEN)+"\"}";
                JsonNode jn = mapper.readTree(user);
                return ok(jn);
            } catch (OSecurityAccessException e){
                if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("UserLogin: " +  ExceptionUtils.getMessage(e));
                return unauthorized("user " + username + " unauthorized");
            } catch (InvalidAppCodeException e) {
                if (BaasBoxLogger.isDebugEnabled()) BaasBoxLogger.debug("UserLogin: " + ExceptionUtils.getMessage(e));
                return badRequest("user " + username + " unauthorized");
            }
        });
    }

}
