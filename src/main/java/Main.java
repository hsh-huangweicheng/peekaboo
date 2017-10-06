
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import analyzer.BasicInfoAnalyzer;
import exporter.TextExporter;
import wos.WosService;

public class Main {

	public static void main(String[] args) throws IOException {

		long timestamp = System.currentTimeMillis();
		File baseDir = Paths.get(System.getProperty("user.dir"), "wos").toFile();
		Collection<File> listFiles = FileUtils.listFiles(baseDir, null, true);

		WosService wosService = new WosService();

		wosService.setExporter(new TextExporter());
	
		String outputDir = System.getProperty("user.dir") + "/output";
		FileUtils.deleteDirectory(new File(outputDir));

		wosService.setOutputDir(outputDir);
		wosService.addAnalyzer(new BasicInfoAnalyzer());

		wosService.parse(listFiles);

		System.out.println((System.currentTimeMillis() - timestamp) / 1000);

	}
}
