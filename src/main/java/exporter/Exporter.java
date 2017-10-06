package exporter;

import utils.Table;

public interface Exporter {
	public void export(Table[] table, String filePath);
}
