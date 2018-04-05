package analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import table.CategoryTable;
import table.Table;
import utils.Utils;
import wos.WosRecord;

public class CoperateAnalyzer implements Analyzer {

    private CategoryTable coperateCountTable = new CategoryTable("合作论文数量", "年份");

    private CategoryTable otherCountryCountTable = new CategoryTable("合作国家", "国家");

    private CategoryTable africaCountryCountTable = new CategoryTable("非洲国家论文合作率", "国家");

    private CategoryTable coperateInOutCountTable = new CategoryTable("图 3 2 2：非洲地区论文合作情况", "国家");

    private CategoryTable coperateInOutSubjectTable = new CategoryTable("非洲按学科合作", "学科");
    private CategoryTable firstCountrySubjectCountTable = new CategoryTable("学科非洲主导", "学科");

    private CategoryTable firstCountryCountTable = new CategoryTable("主导国家（非洲国家也算）", "主导国家");

    private CategoryTable firstCountryCountByYearTable = new CategoryTable("主导国家（非洲国家也算，按十年）", "主导国家");

    private CategoryTable africaCountryAndChinaTable = new CategoryTable("中国与非洲国家合作情况", "非洲国家");

    private CategoryTable chinaFirstCountryCountTable = new CategoryTable("中国主导", "年份");

    private CategoryTable citedCountByYearTable = new CategoryTable("被引", "年份");

    private CategoryTable noneCitedCountBySubjectTable = new CategoryTable("零被引学科", "学科");

    private CategoryTable firstCountryByDurationTable = new CategoryTable("各国主导被引（按时间段）", "年份");

    private CategoryTable citedCountByCountryTable = new CategoryTable("非洲各国被引(按年)", "年份");

    private Set topCountrySet = new HashSet<String>(
            Arrays.asList(new String[]{"美国", "法国", "英国", "德国", "沙特阿拉伯", "加拿大", "澳大利亚", "意大利", "荷兰", "比利时", "瑞士", "西班牙", "日本", "瑞典", "印度", "中国"}));

    @Override
    public void scan(WosRecord wosRecord) {

        int year = Integer.parseInt(wosRecord.getYear());
        if (1977 <= year && year <= 2016) {

            coperateCount(wosRecord);

            otherCountryCount(wosRecord);

            africaCountryCount(wosRecord);

            coperateInOutCount(wosRecord);

            firstCountryCount(wosRecord);

            chinaFirstCountryCount(wosRecord);

            citedCountByYear(wosRecord);

            africaCountryAndChina(wosRecord);

            firstCountryCountByYear(wosRecord);

            firstCountryByDuration(wosRecord);

            citedCountByCountry(wosRecord);

            firstCountrySubjectCountTable(wosRecord);

            noneCitedCountBySubjectTable(wosRecord);
        }
    }

    private void noneCitedCountBySubjectTable(WosRecord wosRecord) {
        wosRecord.getSubjectList().forEach(subject -> {
            noneCitedCountBySubjectTable.increase(subject, "总篇次");
            if (0 == wosRecord.getCitedTimes()) {
                noneCitedCountBySubjectTable.increase(subject, "零被引篇次");
            }
        });
    }

    private void firstCountrySubjectCountTable(WosRecord wosRecord) {
        String firstCountry = wosRecord.getFirstCountry();
        boolean isAfrica = Utils.isAfrica(firstCountry);
        boolean isChina = "中国".equals(firstCountry);

        wosRecord.getSubjectList().forEach(subject -> {
            firstCountrySubjectCountTable.increase(subject, "总篇次");

            if (isAfrica) {
                firstCountrySubjectCountTable.increase(subject, "非洲主导篇次");
            } else {
                firstCountrySubjectCountTable.increase(subject, "非洲外主导篇次");
            }

            if (isChina) {
                firstCountrySubjectCountTable.increase(subject, "中国主导篇次");
            }
        });
    }

    private void citedCountByCountry(WosRecord wosRecord) {
        String year = wosRecord.getYear();
        String firstCountry = wosRecord.getFirstCountry();
        int citedTimes = wosRecord.getCitedTimes();

        if (Utils.isAfrica(firstCountry)) {
            citedCountByCountryTable.increase(firstCountry + "(主导)", year + "(被引)", citedTimes);
            citedCountByCountryTable.increase(firstCountry + "(主导)", year + "(篇数)");
        }

        wosRecord.getAfricaCountryList().parallelStream().forEach(country -> {
            citedCountByCountryTable.increase(country, year + "(被引)", citedTimes);
            citedCountByCountryTable.increase(country, year + "(篇数)");
        });
    }

    private void firstCountryByDuration(WosRecord wosRecord) {
        firstCountryByDurationTable.increase(wosRecord.getFirstCountry(), Utils.getYearGroup(wosRecord.getYear()) + "(主导)");
        wosRecord.getCountryList().forEach(country -> {
            firstCountryByDurationTable.increase(country, Utils.getYearGroup(wosRecord.getYear()) + "(总篇次)");
            if (Utils.isAfrica(country)) {
                firstCountryByDurationTable.increase(country, "非洲", 0);
            }
        });
    }

    private void firstCountryCountByYear(WosRecord wosRecord) {
        String firstCountry = wosRecord.getFirstCountry();
        firstCountryCountByYearTable.increase(firstCountry, Utils.getYearGroup(wosRecord.getYear()));
    }

    private void africaCountryAndChina(WosRecord wosRecord) {
        if (wosRecord.getCountryList().contains("中国")) {
            wosRecord.getAfricaCountryList().forEach(africaCountry -> {
                africaCountryAndChinaTable.increase(africaCountry, wosRecord.getYear());
            });
        }
    }

    private void citedCountByYear(WosRecord wosRecord) {
//		int citedTimes = wosRecord.getCitedTimes();
//		String year = wosRecord.getYear();
//
//		int africaCount = wosRecord.getAfricaCountryList().size();
//		List<String> countryList = wosRecord.getCountryList();
//		int otherCount = countryList.size() - africaCount;
//
//		String firstCountry = wosRecord.getFirstCountry();
//
//		citedCountByYearTable.increase(year, "总被引", citedTimes);
//		citedCountByYearTable.increase(year, "总篇数");
//
//		if (topCountrySet.contains(firstCountry)) {
//			citedCountByYearTable.increase(year, firstCountry + "主导被引", citedTimes);
//			citedCountByYearTable.increase(year, firstCountry + "主导篇数");
//		}
//
//		countryList.forEach(country -> {
//			if (topCountrySet.contains(country)) {
//				citedCountByYearTable.increase(year, country + "合作被引", citedTimes);
//				citedCountByYearTable.increase(year, country + "合作篇数");
//			}
//		});
//
//		if (Utils.isAfrica(firstCountry)) {
//			citedCountByYearTable.increase(year, "非洲主导被引", citedTimes);
//			citedCountByYearTable.increase(year, "非洲主导篇数");
//		}
//
//		if (countryList.size() > 1) {
//			citedCountByYearTable.increase(year, "合作被引", citedTimes);
//			citedCountByYearTable.increase(year, "合作篇数");
//
//			if (0 == otherCount) {
//				citedCountByYearTable.increase(year, "仅非洲内合作被引", citedTimes);
//				citedCountByYearTable.increase(year, "仅非洲内合作篇数");
//			} else if (1 == africaCount) {
//				citedCountByYearTable.increase(year, "仅非洲外合作被引", citedTimes);
//				citedCountByYearTable.increase(year, "仅非洲外合作篇数");
//			} else {
//				citedCountByYearTable.increase(year, "非洲内外都有合作被引", citedTimes);
//				citedCountByYearTable.increase(year, "非洲内外都有合作篇数");
//			}
//
//		} else {
//			citedCountByYearTable.increase(year, "无合作被引", citedTimes);
//			citedCountByYearTable.increase(year, "无合作篇数");
//		}
//
//		if (0 == citedTimes) {
//			citedCountByYearTable.increase(year, "篇数(0被引)");
//
//			if (topCountrySet.contains(firstCountry)) {
//				citedCountByYearTable.increase(year, firstCountry + "主导篇数(0被引)");
//			}
//
//			countryList.forEach(country -> {
//				if (topCountrySet.contains(country)) {
//					citedCountByYearTable.increase(year, country + "合作篇数(0被引)");
//				}
//			});
//
//			if (Utils.isAfrica(firstCountry)) {
//				citedCountByYearTable.increase(year, "非洲主导篇数(0被引)");
//			}
//
//			if (countryList.size() > 1) {
//				citedCountByYearTable.increase(year, "合作篇数(0被引)");
//
//				if (0 == otherCount) {
//					citedCountByYearTable.increase(year, "仅非洲内合作篇数(0被引)");
//				} else if (1 == africaCount) {
//					citedCountByYearTable.increase(year, "仅非洲外合作篇数(0被引)");
//				} else {
//					citedCountByYearTable.increase(year, "非洲内外都有合作篇数(0被引)");
//				}
//
//			} else {
//				citedCountByYearTable.increase(year, "无合作篇数(0被引)");
//			}
//		}

    }

    private void chinaFirstCountryCount(WosRecord wosRecord) {
        String firstCountry = wosRecord.getFirstCountry();
        String year = wosRecord.getYear();

        if (wosRecord.hasChineseAuthor()) {
            chinaFirstCountryCountTable.increase(year, "中非合作篇数");
        }

        if ("中国".equals(firstCountry)) {
            chinaFirstCountryCountTable.increase(year, "中国主导篇数");
        }
    }

    private void firstCountryCount(WosRecord wosRecord) {
        String firstCountry = wosRecord.getFirstCountry();

        firstCountryCountTable.increase(firstCountry, "篇次");

        if (Utils.isAfrica(firstCountry)) {
            firstCountryCountTable.increase(firstCountry, "是否非洲", 0);
        }
    }

    private void coperateInOutCount(WosRecord wosRecord) {
        coperateInOut(wosRecord, wosRecord.getSubjectList(), coperateInOutSubjectTable);
        coperateInOut(wosRecord, wosRecord.getAfricaCountryList(), coperateInOutCountTable);
    }

    private void coperateInOut(WosRecord wosRecord, List<String> list, CategoryTable table) {
        List<String> africaCountryList = new ArrayList<>();
        List<String> otherCountryList = new ArrayList<>();
        List<String> countryList = wosRecord.getCountryList();

        countryList.forEach(country -> {
            if (Utils.isAfrica(country)) {
                africaCountryList.add(country);
            } else {
                otherCountryList.add(country);
            }
        });

        list.forEach(africaCountry -> {
            if (countryList.size() > 1) {
                table.increase(africaCountry, "合作");

                if (0 == otherCountryList.size()) {
                    table.increase(africaCountry, "仅非洲内合作");
                } else if (1 == africaCountryList.size()) {
                    table.increase(africaCountry, "仅非洲外合作");
                } else {
                    table.increase(africaCountry, "非洲内外都有合作");
                }

                if (otherCountryList.size() > 0) {
                    table.increase(africaCountry, "国际合作");
                }

                if (otherCountryList.contains("中国")) {
                    table.increase(africaCountry, "中国合作");
                }
            } else {
                table.increase(africaCountry, "无合作");
            }
        });
    }

    private void africaCountryCount(WosRecord wosRecord) {
        int countryCount = wosRecord.getCountryList().size();

        africaCountryCountTable.increase("非洲", "论文总篇数");
        if (countryCount > 1) {
            africaCountryCountTable.increase("非洲", "合作论文篇数");
        }

        wosRecord.getAfricaCountryList().forEach(country -> {
            africaCountryCountTable.increase(country, "论文总篇数");

            if (countryCount > 1) {
                africaCountryCountTable.increase(country, "合作论文篇数");
            }
        });
    }

    private void otherCountryCount(WosRecord wosRecord) {
        wosRecord.getCountryList().parallelStream().filter(country -> {
            return !Utils.isAfrica(country);
        }).forEach(country -> {
            otherCountryCountTable.increase(country, "篇数");
            otherCountryCountTable.increase(country, wosRecord.getYear());
        });
    }

    private void coperateCount(WosRecord wosRecord) {
        String year = wosRecord.getYear();
        int countryCount = wosRecord.getCountryList().size();

        coperateCountTable.increase(year, "论文总量");

        if (countryCount > 1) {

            coperateCountTable.increase(year, "合作论文数量");

            switch (countryCount) {
                case 2:
                    coperateCountTable.increase(year, "双边合作");
                    break;
                case 3:
                    coperateCountTable.increase(year, "三边合作");
                    break;
                default:
                    coperateCountTable.increase(year, "多边合作");
                    break;
            }

            if (wosRecord.hasChineseAuthor()) {
                coperateCountTable.increase(year, "中非合作论文数量");
            }
        }
    }

    @Override
    public Table[] getTables() {
        return new Table[]{citedCountByCountryTable, firstCountryCountByYearTable, firstCountryByDurationTable, coperateCountTable, otherCountryCountTable,
                africaCountryCountTable, coperateInOutCountTable, firstCountryCountTable, chinaFirstCountryCountTable, citedCountByYearTable,
                africaCountryAndChinaTable, coperateInOutSubjectTable, firstCountrySubjectCountTable, noneCitedCountBySubjectTable};
    }

    @Override
    public String getName() {
        return "";
    }

}
