package com.beerbars;

public enum ServerConfigurationEnum {
	DATABASE_URL("odb.url"), 
	DATABASE_USERNAME("odb.user"), 
	DATABASE_PASSWORD("odb.password");

	private String value;
	
	private ServerConfigurationEnum(String value) {
		this.value = value;
	}

	public String toString() {
		return value;
	}
}
