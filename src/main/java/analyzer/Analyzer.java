package analyzer;

import table.Table;
import wos.WosRecord;

public interface Analyzer {

	public void scan(WosRecord wosRecord);

	public Table[] getTables();

	public String getName();
}
