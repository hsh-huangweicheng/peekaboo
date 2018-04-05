package exporter;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import table.Table;

public class CSVExporter implements Exporter {

	private static final String NEW_LINE_SEPARATOR = "\n";

	@Override
	public void export(Table[] tables, String filePath) {

		Arrays.stream(tables).parallel().forEach(table -> {
			// 创建 CSVFormat
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
			String outputFilePath = filePath + table.getName() + ".csv";

			try (FileWriter fileWriter = new FileWriter(filePath + table.getName() + ".csv");
					CSVPrinter csvFilePrinter = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath, true), "GBK")),
							csvFileFormat)) {

				// 创建CSV文件头
				csvFilePrinter.printRecord((Object) table.getFieldNames());

				table.getTrList().parallelStream().forEach(trList -> {
					if (trList.size() >= 0) {
						try {
							csvFilePrinter.printRecord(trList);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

}
