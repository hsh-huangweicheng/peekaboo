package analyzer;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import utils.ResourceUtils;
import utils.Table;
import wos.WosRecord;

public class BasicInfoAnalyzer extends Analyzer {

	private Table countryTable = new Table("国家论文数量", new String[] { "年", "国家", "非洲", "计数" });

	private Table otherInfomationTable = new Table("其它", new String[] { "描述", "计数" });

	private Table firstAuthorCountryTable = new Table("第一作者国家", new String[] { "年", "国家", "非洲", "计数" });

	private Table coperateTable = new Table("合作", new String[] { "年", "国家", "非洲", "计数" });

	private Table yearAverageCitedTable = new Table("年度平均被引次数", new String[] { "年份", "平均被引次数" });

	private Table coperateYearAverageCitedTable = new Table("合作年度平均被引次数", new String[] { "年份", "合作类型", "平均被引次数" });

	@Override
	public void scan(WosRecord wosRecord) {

		// 每个国家的论文数量
		countCountry(wosRecord);

		countFirstAuthorCountry(wosRecord);

		countCoperate(wosRecord);

		countOtherInfomation(wosRecord);

		averageCitedTimes(wosRecord);

		coperateAverageCitedTimes(wosRecord);
	}

	private void coperateAverageCitedTimes(WosRecord wosRecord) {
		List<String> countryList = wosRecord.getCountryList();
		Stream<String> countryListStream = countryList.stream();
		long africaCount = countryListStream.filter(ResourceUtils::isAfrica).count();
		long otherCount = countryList.size() - africaCount;

		int citedTimes = wosRecord.getCitedTimes();
		
		if (0 == otherCount) {
			if (1 == africaCount) {
				// 无合作
				coperateYearAverageCitedTable.add(wosRecord.getYear()).add("无合作").average(citedTimes);
			} else {
				coperateYearAverageCitedTable.add(wosRecord.getYear()).add("非洲内合作").average(citedTimes);
			}
		} else {
			// 与非洲外合作
			coperateYearAverageCitedTable.add(wosRecord.getYear()).add("非洲外合作").average(citedTimes);
		}

		if (countryList.contains("中国")) {
			// 与中国合作
			coperateYearAverageCitedTable.add(wosRecord.getYear()).add("中国").average(citedTimes);
		}
	}

	private void averageCitedTimes(WosRecord wosRecord) {
		yearAverageCitedTable.add(wosRecord.getYear()).average(wosRecord.getCitedTimes());
	}

	private void countCoperate(WosRecord wosRecord) {
		List<String> countryList = wosRecord.getCountryList();
		String firstCountry = countryList.get(0);

		for (int i = 1; i < countryList.size(); i++) {
			coperateTable.add(wosRecord.getYear()).add(firstCountry).add(ResourceUtils.isAfrica(firstCountry)).add(countryList.get(i)).increase();
		}
	}

	private void countFirstAuthorCountry(WosRecord wosRecord) {
		// 第一作者
		String firstCountry = wosRecord.getCountryList().get(0);
		firstAuthorCountryTable.add(wosRecord.getYear()).add(firstCountry).add(ResourceUtils.isAfrica(firstCountry)).increase();
	}

	private void countOtherInfomation(WosRecord wosRecord) {
		// 论文数量总计,RP字段计数
		otherInfomationTable.add("Total").increase();
		if (!StringUtils.isEmpty(wosRecord.getString("RP"))) {
			otherInfomationTable.add("RP").increase();
		}

		if (ResourceUtils.isAfrica(wosRecord.getFirstCountry())) {
			otherInfomationTable.add("第一作者是非洲").increase();
		}
	}

	private void countCountry(WosRecord wosRecord) {
		wosRecord.getCountryList().forEach((country) -> {
			countryTable.add(wosRecord.getYear()).add(country).add(ResourceUtils.isAfrica(country)).increase();
		});
	}

	@Override
	public Table[] getTables() {
		return new Table[] { countryTable, otherInfomationTable, firstAuthorCountryTable, coperateTable, yearAverageCitedTable, coperateYearAverageCitedTable };
	}

	@Override
	public String getName() {
		return "基本信息";
	}

}
