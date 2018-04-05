package analyzer;

import table.Table;
import wos.WosRecord;

public class DBAnalyzer implements Analyzer {



    @Override
    public void scan(WosRecord wosRecord) {

    }

    @Override
    public Table[] getTables() {
        return new Table[0];
    }

    @Override
    public String getName() {
        return null;
    }
}
