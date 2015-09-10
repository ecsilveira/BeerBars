package com.beerbars.security;

import java.util.Enumeration;
import java.util.List;

import com.google.common.collect.ImmutableMap;

/**
 * Interface para o Token Provider
 * @author Chico
 *
 */
public interface SessionTokenProviderInterface {

    /**
     * Cria uma nova sessao, para posterior uso
     * @param AppCode
     * @param username
     * @param Password
     * @return
     */
    public ImmutableMap<SessionKeysEnum, ? extends Object> setSession(String AppCode, String username, String Password);

    /**
     * Retorna as informacoes da sessao pelo token
     * @param token
     * @return
     */
    public ImmutableMap<SessionKeysEnum, ? extends Object> getSession(String token);

    /**
     * Retorna a Sessao Atual
     * @return
     */
    public ImmutableMap<SessionKeysEnum, ? extends Object> getCurrent();

    /**
     * Remove a Sessao
     * @param token
     */
    public void removeSession(String token);

    /**
     * Seta o Tempo do Timeout em Milisegundos
     * @param timeoutInMilliseconds
     */
    public void setTimeout(long timeoutInMilliseconds);

    /**
     * Retorna os Tokens
     * @return
     */
    public Enumeration<String> getTokens();

    /**
     * Retorna as Sessoes do Usuario
     * @param username
     * @return
     */
    public List<ImmutableMap<SessionKeysEnum, ? extends Object>> getSessions(String username);

    /**
     * Destroy o Session Token Provider
     */
    public void destroySessionTokenProvider();

}
