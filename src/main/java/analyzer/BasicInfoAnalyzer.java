package analyzer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import table.CountTable;
import table.NormalTable;
import table.Table;
import utils.ResourceUtils;
import wos.WosRecord;

public class BasicInfoAnalyzer implements Analyzer {

	private CountTable countryTable = new CountTable("国家论文数量", new String[] { "年", "国家", "非洲", "计数" });

	private CountTable otherInfomationTable = new CountTable("其它", new String[] { "描述", "计数" });

	private CountTable firstAuthorCountryTable = new CountTable("第一作者国家", new String[] { "年", "国家", "非洲", "计数" });

	private CountTable coperateTable = new CountTable("合作", new String[] { "年", "国家", "非洲", "计数" });

	private CountTable yearAverageCitedTable = new CountTable("年度平均被引次数", new String[] { "年份", "平均被引次数" });

	private CountTable coperateYearAverageCitedTable = new CountTable("合作年度平均被引次数", new String[] { "年份", "合作类型", "平均被引次数" });

	// 非洲各国合作论文数量
	private CountTable coperateCountTable = new CountTable("合作统计", new String[] { "合作类型", "计数" });

	// 基金信息
	private NormalTable foundTable = new NormalTable("基金资助机构和授权号", new String[] { "年份", "UT", "基金资助机构" });

	@Override
	public void scan(WosRecord wosRecord) {
		String year = wosRecord.getYear();

		// 每个国家的论文数量
		countCountry(wosRecord);

		countFirstAuthorCountry(wosRecord);

		countCoperateCountry(wosRecord);

		countOtherInfomation(wosRecord);

		averageCitedTimes(wosRecord);

		coperateAverageCitedTimes(wosRecord);

		coperateCount(wosRecord);

		found(wosRecord);
	}

	private void found(WosRecord wosRecord) {
		String fu = wosRecord.getString("FU");
		String ut = wosRecord.getString("UT");
		String year = wosRecord.getYear();

		if (StringUtils.isEmpty(fu)) {
			return;
		}
		Arrays.stream(fu.split(";")).parallel().map(line -> {

			int index = line.lastIndexOf("[");

			if (index > 0) {
				return line.substring(0, index).trim();
			} else {
				return line.replaceAll("[\\[\\]]", "");
			}

		}).forEach(fund -> {
			if (!StringUtils.isEmpty(fund)) {
				this.foundTable.add(year, ut, fund.trim());
			}
		});

	}

	private void coperateCount(WosRecord wosRecord) {
		List<String> countryList = wosRecord.getCountryList();
		switch ("" + countryList.size()) {
			case "1":
				coperateCountTable.add("单边").increase();
				break;
			case "2":
				coperateCountTable.add("双边").increase();
				if (countryList.contains("中国")) {
					coperateCountTable.add("中国双边").increase();
				}
				break;
			default:
				coperateCountTable.add("多边").increase();
				if (countryList.contains("中国")) {
					coperateCountTable.add("中国多边").increase();
				}
				break;
		}
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

		if (wosRecord.getFirstCountry().equals("中国")) {
			// 中国是第一作者的合作
			coperateYearAverageCitedTable.add(wosRecord.getYear()).add("第一作者是中国").average(citedTimes);
		}

		coperateYearAverageCitedTable.add(wosRecord.getYear()).add("合计").average(citedTimes);
	}

	private void averageCitedTimes(WosRecord wosRecord) {
		yearAverageCitedTable.add(wosRecord.getYear()).average(wosRecord.getCitedTimes());
	}

	private void countCoperateCountry(WosRecord wosRecord) {
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
		return new Table[] { coperateCountTable, countryTable, otherInfomationTable, firstAuthorCountryTable, coperateTable, yearAverageCitedTable,
				coperateYearAverageCitedTable, foundTable };
	}

	@Override
	public String getName() {
		return "基本信息";
	}

}
