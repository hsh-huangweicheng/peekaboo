package exporter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import table.Table;

public class TextExporter implements Exporter {

	@Override
	public void export(Table[] tables, String filePath) {
		String delimeter = "\t";
		for (Table table : tables) {
			List<String> list = new ArrayList<String>();

			list.add(StringUtils.join(table.getFieldNames(), delimeter));

			for (List<String> trList : table.getTrList()) {
				list.add(StringUtils.join(trList, delimeter));
			}

			try {
				String fileName = filePath + table.getName() + ".txt";
				FileUtils.writeLines(new File(fileName), list);
				System.out.println("write file to " + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
