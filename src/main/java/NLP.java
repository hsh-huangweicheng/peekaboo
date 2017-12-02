import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import analyzer.NlpFXAnalyzer;
import exporter.TextExporter;
import wos.WosService;

public class NLP {
	public static void main(String[] args) throws IOException {
		long timestamp = System.currentTimeMillis();
		File baseDir = Paths.get(System.getProperty("user.dir"), "data").toFile();
		File nlpOutputFile = Paths.get(System.getProperty("user.dir"), "output", "nlp.txt").toFile();

		Collection<File> listFiles = FileUtils.listFiles(baseDir, null, true);

		WosService wosService = new WosService();

		wosService.addExporter(new TextExporter());

		String outputDir = System.getProperty("user.dir") + "/output";

		System.out.println("import from [" + baseDir.getAbsolutePath() + "], export to [" + nlpOutputFile.getAbsolutePath() + "]");

		File outputFile = new File(outputDir);
		outputFile.mkdirs();

		wosService.setOutputDir(outputDir);

		wosService.addAnalyzer(new NlpFXAnalyzer(nlpOutputFile));

		wosService.parse(listFiles);

		System.out.println((System.currentTimeMillis() - timestamp) / 1000);
	}
}
