package table;

import java.util.List;

public abstract class Table {

	private String name;
	private String[] fieldNames;

	public Table(String name, String[] fieldNames) {
		this.name = name;
		this.fieldNames = fieldNames;
	}

	public String getName() {
		return name;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public abstract List<List<String>> getTrList();
}
