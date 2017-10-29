package table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NormalTable implements Table {

	private String name;
	private String[] fieldNames;
	private List<List<String>> trList = new ArrayList<>();

	public NormalTable(String name, String[] fieldNames) {
		this.name = name;
		this.fieldNames = fieldNames;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String[] getFieldNames() {
		return fieldNames;
	}

	public void add(String... strs) {
		trList.add(Arrays.asList(strs));
	}

	public void add(List<String> list) {
		trList.add(list);
	}

	@Override
	public List<List<String>> getTrList() {
		return trList;
	}

}
