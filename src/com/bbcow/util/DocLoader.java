package com.bbcow.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class DocLoader {
	private static Properties p = new Properties();
	public static String target_path;
	
	public static String getString(String key){
		return p.getProperty(key);
	}
	public static int getInteger(String key){
		return Integer.parseInt(getString(key));
	}
	
	public static void load(String path){
		InputStream setting = DocLoader.class.getResourceAsStream("/setting.properties");
		try {
			p.load(setting);
			
			target_path = path;
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				setting.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {

	}
}
