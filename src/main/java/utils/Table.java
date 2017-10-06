package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Table {

	private Map<String, Table> map = new ConcurrentHashMap<>();

	private int count = 1;

	private String[] fieldNames;

	private String name;

	public Table(String name, String[] fieldNames) {
		this.name = name;
		this.fieldNames = fieldNames;
	}

	public Table() {
		this("", new String[] {});
	}

	public String getName() {
		return this.name;
	}

	public String[] getFieldNames() {
		return this.fieldNames;
	}

	public Table add(String value) {
		Table newTable = new Table();
		Table putIfAbsent = this.map.putIfAbsent(value, newTable);

		if (null == putIfAbsent) {
			return newTable;
		} else {
			putIfAbsent.count++;
			return putIfAbsent;
		}
	}

	public List<List<String>> getTrList() {
		List<List<String>> retList = new ArrayList<>();

		for (Entry<String, Table> entry : map.entrySet()) {
			String key = entry.getKey();
			Table childTable = entry.getValue();
			List<List<String>> chilsTrList = childTable.getTrList();

			if (chilsTrList.isEmpty()) {
				List<String> list = new ArrayList<>();
				list.add(key);
				list.add("" + childTable.count);
				retList.add(list);
			} else {
				for (List<String> trList : chilsTrList) {
					List<String> list = new ArrayList<>();
					list.add(key);
					list.addAll(trList);
					retList.add(list);
				}
			}

		}

		return retList;
	}
}
