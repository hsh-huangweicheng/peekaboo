package analyzer;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import utils.ResourceUtils;
import utils.Table;
import wos.WosRecord;

public class BasicInfoAnalyzer extends Analyzer {

	private Table countryTable = new Table("国家论文数量", new String[] { "Country", "Africa", "Count" });

	private Table totalTable = new Table("", new String[] { "描述", "Count" });

	private Table firstAuthorCountryTable = new Table("第一作者国家", new String[] { "Country", "Africa", "Count" });

	@Override
	public void scan(WosRecord wosRecord) {

		List<String> countryList = wosRecord.getCountryList();

		// 每个国家的论文数量
		countCountry(countryList);

		countOtherInfomation(wosRecord);

		countFirstAuthorCountry(countryList);
	}

	private void countFirstAuthorCountry(List<String> countryList) {
		// 第一作者
		String firstCountry = countryList.get(0);
		firstAuthorCountryTable.add(firstCountry).add(ResourceUtils.isAfrica(firstCountry));
	}

	private void countOtherInfomation(WosRecord wosRecord) {
		// 论文数量总计,RP字段计数
		totalTable.add("Total");
		if (!StringUtils.isEmpty(wosRecord.getString("RP"))) {
			totalTable.add("RP");
		}
	}

	private void countCountry(List<String> countryList) {
		countryList.forEach((country) -> {
			countryTable.add(country).add(ResourceUtils.isAfrica(country));
		});
	}

	@Override
	public Table[] getTables() {
		return new Table[] { countryTable, totalTable, firstAuthorCountryTable };
	}

	@Override
	public String getName() {
		return "基本信息";
	}

}
