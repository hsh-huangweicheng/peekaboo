package exporter;

import table.Table;

public interface Exporter {
	public void export(Table[] table, String filePath);
}
