
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import analyzer.BasicInfoAnalyzer;
import exporter.CSVExporter;
import wos.WosService;

public class Main {

	public static void main(String[] args) throws IOException {

		long timestamp = System.currentTimeMillis();
		File baseDir = Paths.get(System.getProperty("user.dir"), "tmp").toFile();
		Collection<File> listFiles = FileUtils.listFiles(baseDir, null, true);

		WosService wosService = new WosService();

		// wosService.setExporter(new TextExporter());
		// wosService.setExporter(new ExcelExporter());
		wosService.setExporter(new CSVExporter());

		String outputDir = System.getProperty("user.dir") + "/output";
		File outputFile = new File(outputDir);
		FileUtils.deleteDirectory(outputFile);
		outputFile.mkdirs();

		wosService.setOutputDir(outputDir);
		// wosService.addAnalyzer(new FXAnalyzer());
		// wosService.addAnalyzer(new RegexFXAnalyzer());
		wosService.addAnalyzer(new BasicInfoAnalyzer());

		wosService.parse(listFiles);

		System.out.println((System.currentTimeMillis() - timestamp) / 1000);

	}
}
