package analyzer;

import table.CategoryTable;
import table.Table;
import utils.Utils;
import wos.WosRecord;

public class InfoAnalyzer implements Analyzer {

	private CategoryTable countryCountTable = new CategoryTable("各国篇次", "国家");

	private CategoryTable countryCountByYearTable = new CategoryTable("各国篇次(十年)", "国家");

	private CategoryTable totalCountTable = new CategoryTable("总篇数", "总篇数");

	@Override
	public void scan(WosRecord wosRecord) {
			countryCount(wosRecord);
	}

	private void countryCount(WosRecord wosRecord) {
		wosRecord.getCountryList().forEach(country -> {
			countryCountTable.increase(country, "篇次");
			if (Utils.isAfrica(country)) {
				countryCountTable.increase(country, "是否非洲", 0);
			}
			countryCountByYearTable.increase(country, Utils.getYearGroup(wosRecord.getYear()));
		});
		totalCountTable.increase("总篇数", "总篇数");
	}

	@Override
	public Table[] getTables() {
		return new Table[] { countryCountTable, countryCountByYearTable, totalCountTable };
	}

	@Override
	public String getName() {
		return "";
	}

}
