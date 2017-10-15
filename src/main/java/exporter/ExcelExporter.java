package exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import utils.Table;

public class ExcelExporter implements Exporter {

	@Override
	public void export(Table[] tables, String filePath) {

		for (Table table : tables) {
			try {

				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet(table.getName());

				this.setHeader(table, sheet);

				for (int i = 0; i < table.getTrList().size(); i++) {
					List<String> list = table.getTrList().get(i);
					HSSFRow row = sheet.createRow(i + 1);

					for (int j = 0; j < list.size(); j++) {
						HSSFCell cell = row.createCell(j);
						cell.setCellValue(list.get(j));
					}
				}

				FileOutputStream fos = new FileOutputStream(new File(filePath + ".xlsx"));
				workbook.write(fos);
				fos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setHeader(Table table, HSSFSheet sheet) {

		HSSFRow row = sheet.createRow(0);
		String[] fieldNames = table.getFieldNames();

		for (int i = 0; i < fieldNames.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(fieldNames[i]);
		}
	}

}
