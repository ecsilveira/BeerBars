package com.beerbars.controllers;

import java.util.Map;

import com.beerbars.ServerConfiguration;
import com.beerbars.db.DBHelper;
import com.beerbars.logging.ServerLogger;
import com.beerbars.security.SessionKeysEnum;
import com.beerbars.security.SessionTokenProviderFactory;
import com.beerbars.util.ServerJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.orientechnologies.orient.core.db.document.ODatabaseDocument;
import com.orientechnologies.orient.core.exception.OSecurityAccessException;

import play.libs.F;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;

/**
 * Classe Controller reponsaveis pelo acesso dos Usuarios tanto para Login, Cadastro, Login via Plugins
 * 
 * @author B35579
 * 
 */
public class Credential extends Controller {

    /**
     * Metodo Responsavel pelo Login do Usuario
     * 
     * @return F.Promise<Result>
     */
    @With ({NoUserCredentialWrapFilterAsync.class})
	public static Promise<Result> login() {
		ServerLogger.debug("Credentials.login() - begin...");

		final String username;
		final String password;
		final String loginData;

		RequestBody body = request().body();
		// BaasBoxLogger.debug ("Login called. The body is: {}", body);
		if (body == null) {
			ServerLogger.warn("Credentials.login() - body == null");
			return F.Promise.pure(badRequest("missing data: is the body x-www-form-urlencoded or application/json? Detected: " + request().getHeader(CONTENT_TYPE)));
		}

		Map<String, String[]> bodyUrlEncoded = body.asFormUrlEncoded();

		if (bodyUrlEncoded != null) {
			if (bodyUrlEncoded.get("username") == null) {
				return F.Promise.pure(badRequest("The 'username' field is missing"));
			} else {
				username = bodyUrlEncoded.get("username")[0];
			}
			if (bodyUrlEncoded.get("password") == null) {
				return F.Promise.pure(badRequest("The 'password' field is missing"));
			} else {
				password = bodyUrlEncoded.get("password")[0];
			}

			if (bodyUrlEncoded.get("login_data") != null) {
				loginData = bodyUrlEncoded.get("login_data")[0];
				ServerLogger.debug("Credentials.login() - login_data " + loginData);
			} else {
				loginData = null;
			}
		} else {
			JsonNode bodyJson = body.asJson();

			if (bodyJson == null) {
				ServerLogger.warn("Credentials.login() - bodyJson == null ");
				return F.Promise.pure(badRequest("missing data : is the body x-www-form-urlencoded or application/json? Detected: "	+ request().getHeader(CONTENT_TYPE)));
			}

			if (bodyJson.get("username") == null) {
				return F.Promise.pure(badRequest("The 'username' field is missing"));
			} else {
				username = bodyJson.get("username").asText();
			}

			if (bodyJson.get("password") == null) {
				return F.Promise.pure(badRequest("The 'password' field is missing"));
			} else {
				password = bodyJson.get("password").asText();
			}

			if (bodyJson.get("login_data") != null) {
				loginData = bodyJson.get("login_data").asText();
				ServerLogger.debug("Credentials.login() - login_data " + loginData);
			} else {
				loginData = null;
			}
		}

		ServerLogger.debug("Credentials.login() - ussername " + username);
		// TODO remover
		ServerLogger.debug("Credentials.login() - password " + password);

		if (ServerConfiguration.getDatabaseUsername().equalsIgnoreCase(username) || ServerConfiguration.getDatabasePassword().equalsIgnoreCase(username)) {
			return F.Promise.pure(forbidden(username + " cannot login"));
		}

        return F.Promise.promise(()->{
            String user;
            
            try (ODatabaseDocument db = DBHelper.openConnection(username,password)){
                
                user = prepareResponseToJson(db.getUser());
                
                //TODO login data é o token dos dispositivos
//                if (loginData != null) {
//                    JsonNode loginInfo = null;
//                    try {
//                        loginInfo = Json.parse(loginData);
//                    } catch (Exception e){
//                        ServerLogger.error("Credentials.login() - erro ao logar", e);
//                        return badRequest("login_data field is not a valid json string");
//                    }
//
//                    Iterator<Entry<String, JsonNode>> it =loginInfo.fields();
//                    HashMap<String, Object> data = new HashMap<String, Object>();
//                    while (it.hasNext()){
//                        Entry<String, JsonNode> element = it.next();
//                        String key=element.getKey();
//                        Object value=element.getValue().asText();
//                        data.put(key,value);
//                    }
//                    UserService.registerDevice(data);
//                }
                ImmutableMap<SessionKeysEnum, ? extends Object> sessionObject = SessionTokenProviderFactory.getSessionTokenProvider().setSession(username, password);
                response().setHeader(SessionKeysEnum.TOKEN.toString(), (String) sessionObject.get(SessionKeysEnum.TOKEN));

                ObjectMapper mapper = ServerJson.mapper();
                //user = user.substring(0,user.lastIndexOf("}")) + ",\""+ SessionKeysEnum.TOKEN.toString()+"\":\""+ (String) sessionObject.get(SessionKeysEnum.TOKEN)+"\"}";
                
                user = "{username = "+ username + ",\""+ SessionKeysEnum.TOKEN.toString()+"\":\""+ (String) sessionObject.get(SessionKeysEnum.TOKEN)+"\"}";
                JsonNode jn = mapper.readTree(user);
                return ok(jn);
            } catch (OSecurityAccessException e){
            	ServerLogger.error("Credentials.login() - erro ao efetuar login\n"+ e.getMessage());
                return unauthorized("user " + username + " unauthorized");
            }
        });
    }
}
