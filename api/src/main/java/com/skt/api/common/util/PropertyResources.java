package com.skt.api.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class PropertyResources { 
	private static PropertyResources instance;
	private static Logger logger = Logger.getLogger(PropertyResources.class.getName()); 
	private Properties properties;

	
	public static PropertyResources getInstance() {
		if (instance == null) {
			instance = new PropertyResources();
			try {
				instance.load();
			} catch (Exception e) {
				return null;
			}
		}

		return instance;
	}
	
	/*
	public PropertyResources() throws Exception {
		try {
			load();
		} catch (Exception e) {
			throw new Exception("Error occured on properties load!");
		}
	}
	*/
	
	private void load() throws FileNotFoundException, IOException {
		String config = System.getProperty("api.config");
		String path = PropertyResources.class.getResource("").getPath();
		if(path.startsWith("/C:") || path.startsWith("/D:"))
			path= path.substring(1);
		String classesPath = path.substring(0, path.lastIndexOf("classes"));
		String configFile = classesPath+"classes/config/"+config+".properties";
		logger.info("properties path="+configFile);
		properties = new Properties();
		InputStream is = new FileInputStream(configFile);
		properties.load(is);
	}

	public static String getLocation() {
		String config = System.getProperty("ocr.config");
		String configFile = "classpath:config/"+config+".properties";
		return configFile;
	}
	
	public void setEnvironment(Properties env){
		this.properties = env;
	}

	public String getProperty(String key) throws Throwable{
		if(properties.getProperty(key)==null) {
			logger.fatal("properties file에 "+key+"항목이 없습니다");
			return null;
		}
		return properties.getProperty(key);
	}

}
