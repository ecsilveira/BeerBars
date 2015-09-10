package com.beerbars;

import play.Configuration;
import play.Play;

public class ServerConfiguration {

	public static Configuration configuration = Play.application().configuration();
	
	public static String getDatabaseURL(){
		return configuration.getString(ServerConfigurationEnum.DATABASE_URL.toString());
	}
	
	public static String getDatabaseUsername(){
		return configuration.getString(ServerConfigurationEnum.DATABASE_USERNAME.toString());
	}
	
	public static String getDatabasePassword(){
		return configuration.getString(ServerConfigurationEnum.DATABASE_PASSWORD.toString());
	}
}
