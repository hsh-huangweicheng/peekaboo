package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class ResourceUtils {

	private static Properties countryProps = new Properties();

	private static Set<String> africaSet = new HashSet<>();

	static {
		try {
			countryProps = loadAsProperties("country.properties");
			africaSet = loadAsSet("africa.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getCountryName(String name) {
		return countryProps.getProperty(name.replace(" ", "_"));
	}

	public static boolean isAfrica(String name) {
		return africaSet.contains(name);
	}

	private static Properties loadAsProperties(String path) throws IOException {
		List<String> lineList = loadAsLines(path);
		Properties props = new Properties();

		lineList.parallelStream().forEach((line) -> {
			String[] splits = line.split("=");
			if (2 == splits.length) {
				props.setProperty(splits[0].trim(), splits[1].trim());
			}
		});

		return props;
	}

	private static Set<String> loadAsSet(String path) throws IOException {
		List<String> lineList = loadAsLines(path);
		return new HashSet<>(lineList);
	}

	private static List<String> loadAsLines(String path) throws IOException {
		String filePath = ClassLoader.getSystemResource(path).getPath();
		File file = new File(filePath);

		return FileUtils.readLines(file, Charset.forName("utf-8"));
	}
}
