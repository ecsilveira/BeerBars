package com.beerbars;

import static play.Logger.debug;
import static play.Logger.info;
import play.Application;
import play.GlobalSettings;

import com.beerbars.logging.ServerLogger;
import com.beerbars.security.SessionTokenProviderFactory;
import com.beerbars.security.SessionTokenProviderInterface;


/**
 * Classe Inicial do Server
 * @author Chico
 *
 */
public class Global extends GlobalSettings {

    SessionTokenProviderInterface stp;
    
    @Override
    public void onStart(Application app) {
        ServerLogger.debug("Global.onStart - begin...");
        
        ServerLogger.info("Global.onStart - iniciando o session token provider");
        stp = SessionTokenProviderFactory.getSessionTokenProvider();
        stp.setTimeout(ServerConfiguration.getSessionTokenTimeout()*60*1000); //minutes * 60 seconds * 1000 milliseconds
    }
    
    @Override
    public void onStop(Application app) {
        ServerLogger.debug("Global.onStop() - begin...");
//      info("BaasBox is shutting down...");
//      try{
//          info("Closing the DB connections...");
//          ODatabaseDocumentPool.global().close();
//          info("Shutting down embedded OrientDB Server");
//          Orient.instance().shutdown();
//          info("...ok");
//      }catch (ODatabaseException e){
//          error("Error closing the DB!",e);
//      }catch (Throwable e){
//          error("!! Error shutting down BaasBox!", e);
//      }
//      info("Destroying session manager...");
      stp.destroySessionTokenProvider();
      ServerLogger.debug("Global.onStop() - parado!");
    }  
    
}
