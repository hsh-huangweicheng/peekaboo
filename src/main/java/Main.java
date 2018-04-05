
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import analyzer.CoperateAnalyzer;
import analyzer.FundAnalyzer;
import analyzer.InfoAnalyzer;
import analyzer.InstAnalyzer;
import analyzer.OtherAnalyzer;
import analyzer.RegexFXAnalyzer;
import exporter.TextExporter;
import wos.WosService;

public class Main {

	public static void main(String[] args) throws IOException {

		long timestamp = System.currentTimeMillis();
		File baseDir = Paths.get(System.getProperty("user.dir"), "data").toFile();
		Collection<File> listFiles = FileUtils.listFiles(baseDir, null, true);

		WosService wosService = new WosService();

		// wosService.setExporter(new ExcelExporter());
		wosService.addExporter(new TextExporter());
		// wosService.addExporter(new CSVExporter());

		String outputDir = System.getProperty("user.dir") + "/output";
		File outputFile = new File(outputDir);
		FileUtils.deleteDirectory(outputFile);
		outputFile.mkdirs();

		wosService.setOutputDir(outputDir);

//		wosService.addAnalyzer(new InfoAnalyzer());
//		wosService.addAnalyzer(new FundAnalyzer());
		wosService.addAnalyzer(new OtherAnalyzer());
//		wosService.addAnalyzer(new CoperateAnalyzer());

		wosService.parse(listFiles);

		System.out.println((System.currentTimeMillis() - timestamp) / 1000);

	}
}
