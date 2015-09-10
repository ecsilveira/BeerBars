package com.beerbars.security;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import play.Logger;
import play.libs.Akka;
import play.mvc.Http;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.Cancellable;

import com.beerbars.logging.ServerLogger;
import com.google.common.collect.ImmutableMap;

/**
 * Session Token Provider - Armazena e controla o timeout das sessoes do server
 * @author Chico
 */
public class SessionTokenProvider implements SessionTokenProviderInterface{

    private static SessionTokenProvider instance;
    private Cancellable sessionCleaner = null;
    
    protected final static ConcurrentHashMap<String, ImmutableMap<SessionKeysEnum, ? extends Object>> sessions = new ConcurrentHashMap<String, ImmutableMap<SessionKeysEnum, ? extends Object>>();
        
    protected long expiresInMilliseconds = 0; //default expiration of session tokens
    protected long sessionClenanerLaunchInMinutes = 60; //the session cleaner will be launch each x minutes.
    
    /**
     * Retorna uma Instancia Singleton do Session Token Provider
     * @return
     */
    public static SessionTokenProviderInterface getSessionTokenProvider() {
        if (instance == null){
            instance = new SessionTokenProvider();
        }
        return instance;
    }
    
    private SessionTokenProvider(){
        startSessionCleaner(sessionClenanerLaunchInMinutes*60000); //converts minutes in milliseconds
    }
    
    private void startSessionCleaner(long timeoutInMilliseconds) {
        sessionCleaner = Akka
                .system()
                .scheduler()
                .schedule(new FiniteDuration(1000, TimeUnit.MILLISECONDS), new FiniteDuration(timeoutInMilliseconds, TimeUnit.MILLISECONDS),
                        new SessionCleaner(), Akka.system().dispatcher());
    }
    
    /**
     * Destroy o Session Token Provider
     */
    public void destroySessionTokenProvider() {
        if (instance != null && instance.sessionCleaner != null) {
            instance.sessionCleaner.cancel();
            Logger.debug("SessionTokenProvider.destroySessionTokenProvider - session cleaner cancelado");
        }
        instance = null;
        
        Logger.debug("SessionTokenProvider.destroySessionTokenProvider - session token provider destruido");
    }

    @Override
    public ImmutableMap<SessionKeysEnum, ? extends Object> setSession(String AppCode, String username, String password) {
        UUID token = UUID.randomUUID();
        ImmutableMap<SessionKeysEnum, ? extends Object> info = ImmutableMap
                .of( SessionKeysEnum.TOKEN, token.toString(), SessionKeysEnum.USERNAME, username, SessionKeysEnum.PASSWORD, password,
                        SessionKeysEnum.EXPIRE_TIME, (new Date()).getTime() + expiresInMilliseconds);
        sessions.put(token.toString(), info);
        return info;
    }

    @Override
    public ImmutableMap<SessionKeysEnum, ? extends Object> getSession(String token) {
        if (isExpired(token)) {
            return null;
        }
        ImmutableMap<SessionKeysEnum, ? extends Object> info = sessions.get(token);

        // atualiza as informacoes da sessao para o novo Expiration Time
        ImmutableMap<SessionKeysEnum, ? extends Object> newInfo = ImmutableMap.of(SessionKeysEnum.TOKEN, token, SessionKeysEnum.USERNAME,
                info.get(SessionKeysEnum.USERNAME), SessionKeysEnum.PASSWORD, info.get(SessionKeysEnum.PASSWORD), SessionKeysEnum.EXPIRE_TIME,
                (new Date()).getTime() + expiresInMilliseconds);
        sessions.put(token, newInfo);
        return newInfo;
    }

    @Deprecated
    @Override
    public ImmutableMap<SessionKeysEnum, ? extends Object> getCurrent() {
        String token = (String) Http.Context.current().args.get("token");
        if (token != null) {
            return sessions.get(token);
        } else {
            return null;
        }
    }

    @Override
    public void removeSession(String token) {
        ServerLogger.debug("SessionTokenProvider.removeSession - " + token + " removed");
        sessions.remove(token);
    }

    @Override
    public void setTimeout(long timeoutInMilliseconds) {
        this.expiresInMilliseconds = timeoutInMilliseconds;
        ServerLogger.debug("SessionTokenProvider.setTimeout - timeout setado para: " + timeoutInMilliseconds);
    }

    @Override
    public Enumeration<String> getTokens() {
        return sessions.keys();
    }

    @Deprecated
    @Override
    public List<ImmutableMap<SessionKeysEnum, ? extends Object>> getSessions(String username) {
        return null;
    }
    
    private boolean isExpired(String token) {
        ImmutableMap<SessionKeysEnum, ? extends Object> info = sessions.get(token);
        if (info == null) {
            return true;
        }
        if (expiresInMilliseconds != 0 && (new Date()).getTime() > (Long) info.get(SessionKeysEnum.EXPIRE_TIME)) {
            removeSession(token);
            return true;
        }
        return false;
    }
    
    protected class SessionCleaner implements Runnable {
        @Override
        public void run() {
            ServerLogger.info("SessionCleaner.run -  begin");
            Enumeration<String> tokens = getTokens();
            
            long totalTokens = 0;
            long removedTokens = 0;
            while (tokens.hasMoreElements()) {
                totalTokens++;
                if (isExpired(tokens.nextElement()))
                    removedTokens++;
            }
            
            ServerLogger.info("SessionCleaner.run - tokens: " + totalTokens + " - removed: " + removedTokens);
            ServerLogger.info("SessionCleaner.run - aguarando proximo ciclo");
        }
    }
}
