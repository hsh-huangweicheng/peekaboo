package analyzer;

import table.CategoryTable;
import table.Table;
import utils.Utils;
import wos.WosRecord;

public class InfoAnalyzer implements Analyzer {

	private CategoryTable countryCountTable = new CategoryTable("各国篇次", "国家");

	private CategoryTable countryCountByYearTable = new CategoryTable("各国篇次(十年)", "国家");

	@Override
	public void scan(WosRecord wosRecord) {

		int year = Integer.parseInt(wosRecord.getYear());

		if (1977 <= year && year <= 2016) {
			countryCount(wosRecord);
		}
	}

	private void countryCount(WosRecord wosRecord) {
		wosRecord.getCountryList().forEach(country -> {
			countryCountTable.increase(country, "篇次");
			countryCountByYearTable.increase(country, Utils.getYearGroup(wosRecord.getYear()));
		});

	}

	@Override
	public Table[] getTables() {
		return new Table[] { countryCountTable, countryCountByYearTable };
	}

	@Override
	public String getName() {
		return "";
	}

}
