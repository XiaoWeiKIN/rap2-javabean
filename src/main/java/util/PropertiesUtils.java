package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by WangXW.
 * Date: 2019/10/24
 */
public class PropertiesUtils {
    private static Properties properties;
    static {
        properties = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("rap.properties");
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return (String) properties.get(key);

    }
}
