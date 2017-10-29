package table;

import java.util.List;

public interface Table {

	public String getName();

	public String[] getFieldNames();

	public List<List<String>> getTrList();
}
