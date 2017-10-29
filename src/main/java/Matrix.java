import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class Matrix {
	public static void main(String[] args) {
		File baseDir = Paths.get(System.getProperty("user.dir"), "matrix").toFile();
		Collection<File> listFiles = FileUtils.listFiles(baseDir, null, true);

		listFiles.stream().filter(file -> {
			return !StringUtils.startsWith(file.getName(), "_") && StringUtils.endsWith(file.getName(), "txt");
		}).forEach(file -> {
			try {
				Map<String, Map<String, String>> map = new HashMap<>();

				List<String> lineList = FileUtils.readLines(file);
				lineList.remove(0);

				Set<String> fieldSetX = new HashSet<>();
				Set<String> fieldSetY = new HashSet<>();

				lineList.stream().forEach(line -> {
					List<String> trList = Arrays.asList(line.split("\\t"));
					String fieldX = trList.get(0);
					String fieldY = trList.get(1);
					String value = trList.get(2);

					fieldSetX.add(fieldX);
					fieldSetY.add(fieldY);

					map.putIfAbsent(fieldX, new HashMap<>());
					map.get(fieldX).put(fieldY, value);
				});

				List<String> fieldListX = new ArrayList(fieldSetX);
				fieldListX.sort((a, b) -> a.compareTo(b));
				List<String> fieldListY = new ArrayList(fieldSetY);
				fieldListY.sort((a, b) -> a.compareTo(b));

				List<String> collect = fieldListX.stream().map(fieldX -> {
					List<String> list = fieldListY.stream().map(fieldY -> {
						return map.get(fieldX).get(fieldY);
					}).collect(Collectors.toList());
					list.add(0, fieldX);
					return StringUtils.join(list, "\t");
				}).collect(Collectors.toList());

				fieldListY.add(0, "");
				collect.add(0, StringUtils.join(fieldListY, "\t"));

				String outputStr = StringUtils.join(collect, "\r\n");

				File outputFile = new File(file.getParent() + "/_" + file.getName());
				FileUtils.writeStringToFile(outputFile, outputStr);
				System.out.println("output matrix file:" + outputFile.getPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
