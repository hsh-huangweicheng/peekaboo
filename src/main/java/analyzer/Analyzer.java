package analyzer;

import utils.Table;
import wos.WosRecord;

public abstract class Analyzer {

	public abstract void scan(WosRecord wosRecord);

	public abstract Table[] getTables();

	public abstract String getName();
}
