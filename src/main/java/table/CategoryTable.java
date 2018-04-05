package table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import edu.stanford.nlp.util.Comparators;

public class CategoryTable extends Table {

    private Map<String, Map<String, Object>> categoryMap = new HashMap<>();

    private Set<String> fieldSet = new HashSet<>();

    private String categoryName;

    public CategoryTable(String name, String categoryName) {
        super(name, new String[]{});
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

        Map<String, Object> fieldMap = new HashMap<>();
        Map<String, Object> putIfAbsentFieldMap = categoryMap.putIfAbsent(categoryValue, fieldMap);
        if (putIfAbsentFieldMap != null) {
            fieldMap = putIfAbsentFieldMap;
        }

        AtomicInteger atomicInteger = new AtomicInteger(0);
        AtomicInteger putIfAbsent = (AtomicInteger) fieldMap.putIfAbsent(fieldName, atomicInteger);

        if (null != putIfAbsent) {
            atomicInteger = putIfAbsent;
        }

        atomicInteger.addAndGet(offset);
    }

    public void increase(String categoryValue, String fieldName, String value) {

        fieldSet.add(fieldName);

        Map<String, Object> fieldMap = new HashMap<>();
        Map<String, Object> putIfAbsentFieldMap = categoryMap.putIfAbsent(categoryValue, fieldMap);

        if (putIfAbsentFieldMap != null) {
            fieldMap = putIfAbsentFieldMap;
        }

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<String, String>(0);
        ConcurrentHashMap<String, String> putIfAbsent = (ConcurrentHashMap<String, String>) fieldMap.putIfAbsent(fieldName, map);

        if (null != putIfAbsent) {
            map = putIfAbsent;
        }

        map.put(value, value);
    }

    @Override
    public List<List<String>> getTrList() {

        return categoryMap.entrySet().parallelStream().map(entry -> {

            List<String> list = new ArrayList<>();
            String category = entry.getKey();
            Map<String, Object> fieldMap = entry.getValue();

            list.add(category);

            fieldSet.stream().forEach(fieldName -> {
                Object val = fieldMap.get(fieldName);
                if (null == val) {
                    list.add("");
                } else {
                    if (val instanceof AtomicInteger) {
                        AtomicInteger atomicInteger = (AtomicInteger) val;
                        list.add("" + atomicInteger.get());
                    } else {
                        ConcurrentHashMap<String, String> map = (ConcurrentHashMap<String, String>) val;
                        list.add("" + map.size());
                    }
                }
            });

            return list;
        }).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        CategoryTable table = new CategoryTable("a", "b");

        table.increase("Field", "A","b");
        table.increase("Field", "A","3");
        table.increase("Field", "A","b");

        System.out.println(table.getTrList());
    }
}
