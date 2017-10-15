package utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Table {

	private Map<String, Table> map = new ConcurrentHashMap<>();

	private int total = 0;

	private int times = 0;

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

	public Table add(Object value) {
		Table newTable = new Table();
		Table putIfAbsent = this.map.putIfAbsent("" + value, newTable);

		if (null == putIfAbsent) {
			return newTable;
		} else {
			return putIfAbsent;
		}
	}

	public void increase(int count) {
		total = total + count;
		times = 1;
	}

	public void increase() {
		increase(1);
	}

	public void average(int count) {
		total = total + count;
		times++;
	}

	public List<List<String>> getTrList() {
		List<List<String>> retList = new ArrayList<>();

		for (Entry<String, Table> entry : map.entrySet()) {
			String key = entry.getKey();
			Table childTable = entry.getValue();
			List<List<String>> childTrList = childTable.getTrList();

			if (childTrList.isEmpty()) {
				// 最后一列是计数
				DecimalFormat df = new DecimalFormat("0.00");
				float result = childTable.total / (float) childTable.times;
				List<String> list = new ArrayList<>();
				list.add(key);
				list.add(df.format(result));
				retList.add(list);
			} else {
				for (List<String> trList : childTrList) {
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
