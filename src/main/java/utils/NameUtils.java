package utils;

import java.io.IOException;
import java.util.Properties;

public class NameUtils {
	private static Properties countryProps = new Properties();
	static {
		try {
			countryProps.load(NameUtils.class.getClassLoader().getResourceAsStream("country.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getCountryName(String name) {
//		return countryProps.getProperty(name.replace(" ", "_"));
		return "测试";
	}
}
