package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static final Properties props = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println("Could not find config.properties file!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEnv() {
        return props.getProperty("env", "local");
    }

    public static String getBaseUrl() {
        String env = getEnv();
        return props.getProperty("base.url." + env, "http://localhost:8080");
    }
    
    public static String getEmailVerifyDeeplink() {
    	String env = getEnv();
    	return props.getProperty("email.verify.deep.link." + env, "uproad://emai-verify");
    }
    
    public static String getForgotPassDeeplink() {
    	String env = getEnv();
    	return props.getProperty("forgot.password.deep.link." + env, "uproad://reset-password");
    }

    public static String getDblink() {
    	String env = getEnv();
    	return props.getProperty("db.link." + env, "jdbc:sqlite:C:/sqlite/db/testdb.db");
    }
    
    public static String getSupportEmail() {
        return props.getProperty("support.email", "support@uproad.com");
    }

    // Generic getter
    public static String get(String key) {
        return props.getProperty(key);
    }

}
