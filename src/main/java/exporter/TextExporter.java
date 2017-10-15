package exporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import utils.Table;

public class TextExporter implements Exporter {

	@Override
	public void export(Table[] tables, String filePath) {

		for (Table table : tables) {
			List<String> list = new ArrayList<String>();

			list.add(StringUtils.join(table.getFieldNames(), "\t"));

			for (List<String> trList : table.getTrList()) {
				list.add(StringUtils.join(trList, "\t"));
			}

			try {
				String fileName = filePath + "_" + table.getName() + ".txt";
				FileUtils.writeLines(new File(fileName), list);
				System.out.println("write file to " + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
