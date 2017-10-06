package analyzer;

import java.util.List;

import utils.Table;
import wos.WosRecord;

public class BasicInfoAnalyzer extends Analyzer {

	private Table countryTable = new Table("Country", new String[] { "Country", "Count" });

	@Override
	public void scan(WosRecord wosRecord) {
		List<String> countryList = wosRecord.getCountryList();
		countryList.forEach(country -> countryTable.add(country));
	}

	@Override
	public Table[] getTables() {
		return new Table[] { this.countryTable };
	}

	@Override
	public String getName() {
		return "Basic Infomation";
	}

}
