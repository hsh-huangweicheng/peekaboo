package table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import edu.stanford.nlp.util.Comparators;

public class CategoryTable extends Table {

	private Map<String, Map<String, AtomicInteger>> categoryMap = new HashMap<>();

	private Set<String> fieldSet = new HashSet<>();

	private String categoryName;

	public CategoryTable(String name, String categoryName) {
		super(name, new String[] {});
		this.categoryName = categoryName;
	}

	public String[] getFieldNames() {

		List<String> list = new ArrayList<>();
		list.add(this.categoryName);
		list.addAll(fieldSet);

		return list.toArray(new String[list.size()]);
	}

	public void increase(String categoryValue, String fieldName) {
		this.increase(categoryValue, fieldName, 1);
	}

	public void increase(String categoryValue, String fieldName, int offset) {

		fieldSet.add(fieldName);

		Map<String, AtomicInteger> fieldMap = new HashMap<>();
		Map<String, AtomicInteger> putIfAbsentFieldMap = categoryMap.putIfAbsent(categoryValue, fieldMap);
		if (putIfAbsentFieldMap != null) {
			fieldMap = putIfAbsentFieldMap;
		}

		AtomicInteger atomicInteger = new AtomicInteger(0);
		AtomicInteger putIfAbsent = fieldMap.putIfAbsent(fieldName, atomicInteger);

		if (null != putIfAbsent) {
			atomicInteger = putIfAbsent;
		}

		atomicInteger.addAndGet(offset);
	}

	@Override
	public List<List<String>> getTrList() {

		return categoryMap.entrySet().parallelStream().map(entry -> {

			List<String> list = new ArrayList<>();
			String category = entry.getKey();
			Map<String, AtomicInteger> fieldMap = entry.getValue();

			list.add(category);

			fieldSet.stream().forEach(fieldName -> {
				AtomicInteger atomicInteger = fieldMap.get(fieldName);
				if (atomicInteger == null) {
					list.add("");
				} else {
					list.add("" + fieldMap.get(fieldName).get());
				}
			});

			return list;
		}).collect(Collectors.toList());

	}

}
