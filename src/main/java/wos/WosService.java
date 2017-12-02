package wos;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import analyzer.Analyzer;
import exporter.Exporter;
import table.Table;

public class WosService {

	private static Set<String> utSet = new ConcurrentSkipListSet<String>();

	private static AtomicInteger count = new AtomicInteger(0);

	private static int total;

	private List<Analyzer> analyzerList = new ArrayList<>();

	private List<Exporter> exporterList = new ArrayList<>();

	private String outputDir;

	public void addExporter(Exporter exporter) {
		this.exporterList.add(exporter);
	}

	public void addAnalyzer(Analyzer analyzer) {
		this.analyzerList.add(analyzer);
	}

	public void parse(Collection<File> listFiles) {
		AtomicInteger count = new AtomicInteger(0);
		listFiles.stream().parallel().forEach(file -> {
			List<WosRecord> recordList = fileToWosRecord(file);

			long timestamp = System.currentTimeMillis();

			this.analyzerList.parallelStream().forEach((analyzer) -> {
				recordList.parallelStream().forEach((wosRecord) -> {
					analyzer.scan(wosRecord);
				});
			});

			System.out.printf("[%s/%s %8dms] %s", count.incrementAndGet(), listFiles.size(), System.currentTimeMillis() - timestamp, file.getAbsolutePath());
		});

		this.analyzerList.forEach((analyzer) -> {
			Table[] tables = analyzer.getTables();
			if (null != tables) {
				this.exporterList.stream().forEach(exporter -> {
					exporter.export(tables, outputDir + "/" + analyzer.getName());
				});
			}
		});
	}

	/**
	 * 璇诲彇鏂囦欢涓殑鎵�鏈夎
	 * 
	 * @param file
	 * @return
	 */
	private List<String> readLines(File file) {
		try {
			List<String> lineList = FileUtils.readLines(file, Charset.forName("utf-8"));
			System.out.println(count.incrementAndGet() + "/" + total + ", " + file.getAbsolutePath());
			return lineList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * 瑙ｆ瀽鍑篧OS璁板綍
	 * 
	 * @param lines
	 * @return
	 */
	private List<WosRecord> fileToWosRecord(File file) {
		List<String> lineList = readLines(file);
		List<WosRecord> wosRecordList = new ArrayList<WosRecord>();
		List<String> list = new ArrayList<>();

		WosRecord wosRecord = null;
		String lastFieldName = null;

		for (int i = 0; i < lineList.size(); i++) {
			String line = lineList.get(i);

			final String fieldName = getFieldName(line);
			final String value = getValue(line);

			if (!StringUtils.isEmpty(fieldName)) {
				if (null != wosRecord) {
					wosRecord.addField(lastFieldName, list);
				}
				list = new ArrayList<String>();
				lastFieldName = fieldName;
			}

			list.add(value);

			switch (fieldName) {
				case "PT":
					wosRecord = new WosRecord(file);
					break;

				case "ER":

					try {
						final String utValue = wosRecord.getString("UT");

						if (!utSet.contains(utValue)) {
							utSet.add(wosRecord.getString("UT"));
							wosRecordList.add(wosRecord);
						}
						wosRecord = null;
					} catch (Exception e) {
						System.err.println("parse error:" + lineList.get(i - 3));
					}

					break;
			}
		}

		return wosRecordList;
	}

	private static String getFieldName(String line) {
		Pattern pattern = Pattern.compile("^([A-Z\\d]{2})( |$)");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

	private static String getValue(String line) {
		if (line.length() >= 3) {
			return line.substring(3);
		}
		return "";
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
}
