package analyzer;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import table.CategoryTable;
import table.Table;
import utils.Utils;
import wos.WosRecord;

public class FundAnalyzer implements Analyzer {

	private CategoryTable articleCountOfYearTable = new CategoryTable("非洲国家基金项目资助情况", "年份");

	private CategoryTable articleCountOfCountryTable = new CategoryTable("非洲各国论文受资助情况", "国家");

	private CategoryTable articleCitedByYearTable = new CategoryTable("论文被引次数", "年份");

	private CategoryTable fundArticleCountByYearTable = new CategoryTable("我国基金对非洲资助情况(年份)", "年份");

	private CategoryTable fundArticleCountByCountryTable = new CategoryTable("我国基金对非洲资助情况(国家)", "国家");

	private CategoryTable noneCitedArticleCountByCountryTable = new CategoryTable("非洲零被引基金情况", "国家");

	private CategoryTable fundCountOfSujectTable = new CategoryTable("非洲基金资助学科", "学科");

	private CategoryTable wcTable = new CategoryTable("研究方向", "研究方向");

	@Override
	public void scan(WosRecord wosRecord) {
		int year = Integer.parseInt(wosRecord.getYear());

		if (2009 <= year && year <= 2016) {
			// 每年的论文
			articleCountOfYear(wosRecord);

			// 每个国家的论文
			articleCountOfCountry(wosRecord);

			articleCitedByYear(wosRecord);

			// 我国基金对非洲资助情况
			fundArticleCountByYear(wosRecord);

			fundArticleCountByCountry(wosRecord);

			noneCitedArticleCountByCountry(wosRecord);

			// 研究方向按年统计
			wc(wosRecord);

			fundCountOfSuject(wosRecord);
		}

	}

	private void fundCountOfSuject(WosRecord wosRecord) {
		wosRecord.getSubjectList().forEach(subject -> {
			fundCountOfSujectTable.increase(subject, "论文数量");
			if (wosRecord.getFoundList().size() > 0) {
				fundCountOfSujectTable.increase(subject, "基金资助论文数量");
				if (wosRecord.hasChineseFund()) {
					fundCountOfSujectTable.increase(subject, "中国基金资助论文数量");
				}
			}
		});

	}

	private void noneCitedArticleCountByCountry(WosRecord wosRecord) {
		if (0 == wosRecord.getCitedTimes()) {
			wosRecord.getCountryList().forEach(country -> {
				noneCitedArticleCountByCountryTable.increase(country, "零被引论文总量");

				if (wosRecord.getFoundList().size() > 0) {
					noneCitedArticleCountByCountryTable.increase(country, "基金资助论文篇数");

					if (wosRecord.hasChineseFund()) {
						noneCitedArticleCountByCountryTable.increase(country, "中国基金资助");
					}
				}
			});
		}
	}

	private void wc(WosRecord wosRecord) {
		String wc = wosRecord.getString("WC");
		String year = wosRecord.getYear();
		Arrays.stream(wc.split(";")).map(String::trim).filter(StringUtils::isNotEmpty).forEach(str -> {
			if (Utils.hasChineseFund(wosRecord)) {
				this.wcTable.increase(str, "中国-" + year);
			}

			this.wcTable.increase(str, year);
		});
	}

	private void fundArticleCountByCountry(WosRecord wosRecord) {
		wosRecord.getAfricaCountryList().forEach(country -> {
			this.fundArticleCountByCountryTable.increase(country, "论文总量");
			if (!wosRecord.getFoundList().isEmpty()) {
				this.fundArticleCountByCountryTable.increase(country, "资助论文数量");
			}

			if (wosRecord.hasChineseFund()) {
				this.fundArticleCountByCountryTable.increase(country, "中国资助论文数量");
			}
		});
	}

	private void fundArticleCountByYear(WosRecord wosRecord) {
		if (wosRecord.hasChineseFund()) {
			this.fundArticleCountByYearTable.increase(wosRecord.getYear(), "主要基金项目资助论文篇数");
		}
	}

	private void articleCitedByYear(WosRecord wosRecord) {
		boolean hasFound = !wosRecord.getFoundList().isEmpty();
		int citedTimes = wosRecord.getCitedTimes();
		String year = wosRecord.getYear();

		this.articleCitedByYearTable.increase(year, "记录");
		this.articleCitedByYearTable.increase(year, "论文被引次数", citedTimes);
		if (hasFound) {
			this.articleCitedByYearTable.increase(year, "有基金记录");
			this.articleCitedByYearTable.increase(year, "有基金论文被引次数", citedTimes);
		} else {
			this.articleCitedByYearTable.increase(year, "无基金记录");
			this.articleCitedByYearTable.increase(year, "无基金论文被引次数", citedTimes);
		}

		if (0 == citedTimes) {
			this.articleCitedByYearTable.increase(year, "0被引-记录");
			if (hasFound) {
				this.articleCitedByYearTable.increase(year, "0被引-有基金记录");
			} else {
				this.articleCitedByYearTable.increase(year, "0被引-无基金记录");
			}
		}

		if (hasFound && Utils.hasChineseFund(wosRecord)) {
			this.articleCitedByYearTable.increase(year, "中国-记录");
			this.articleCitedByYearTable.increase(year, "中国-论文被引次数", citedTimes);

			if (0 == citedTimes) {
				this.articleCitedByYearTable.increase(year, "中国-0被引-记录");
			}
		}

	}

	private void articleCountOfCountry(WosRecord wosRecord) {
		boolean hasFound = !wosRecord.getFoundList().isEmpty();
		boolean hasChineseFund = wosRecord.hasChineseFund();
		wosRecord.getCountryList().stream().forEach(country -> {
			// 只统计非洲国家
			if (Utils.isAfrica(country)) {
				articleCountOfCountryTable.increase(country, "论文总量");
				if (hasFound) {
					articleCountOfCountryTable.increase(country, "基金项目资助论文");
				}
				if (hasChineseFund) {
					articleCountOfCountryTable.increase(country, "中国基金资助");
				}
			}

		});

	}

	private void articleCountOfYear(WosRecord wosRecord) {
		articleCountOfYearTable.increase(wosRecord.getYear(), "论文总篇数");

		if (!wosRecord.getFoundList().isEmpty()) {
			if (wosRecord.hasChineseFund()) {
				articleCountOfYearTable.increase(wosRecord.getYear(), "中国基金资助");
			}
			articleCountOfYearTable.increase(wosRecord.getYear(), "受资助论文");
		}
	}

	@Override
	public Table[] getTables() {
		return new Table[] { articleCountOfYearTable, articleCountOfCountryTable, articleCitedByYearTable, fundArticleCountByYearTable,
				fundArticleCountByCountryTable, wcTable, noneCitedArticleCountByCountryTable, fundCountOfSujectTable };
	}

	@Override
	public String getName() {
		return "";
	}

}
