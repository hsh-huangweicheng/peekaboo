package analyzer;

import java.util.List;

import utils.ResourceUtils;
import utils.Table;
import wos.WosRecord;

public class CoperateAnalyzer extends Analyzer {


	@Override
	public void scan(WosRecord wosRecord) {


	}

	@Override
	public Table[] getTables() {
		return new Table[] {  };
	}

	@Override
	public String getName() {
		return "Coperation";
	}

}
