package analyzer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import table.CategoryTable;
import table.Table;
import utils.Utils;
import wos.WosRecord;

import javax.rmi.CORBA.Util;

public class OtherAnalyzer implements Analyzer {

    public OtherAnalyzer() {
        map.keySet().forEach((name) -> {
            tableMap.put(name, new CategoryTable(name, name));
        });
    }

    @Override
    public void scan(WosRecord wosRecord) {
        map.keySet().forEach((name) -> {
            map.get(name).accept(tableMap.get(name), wosRecord);
        });
    }

    @Override
    public Table[] getTables() {
        return tableMap.values().toArray(new CategoryTable[tableMap.size()]);
    }

    @Override
    public String getName() {
        return "";
    }

    private Map<String, BiConsumer<CategoryTable, WosRecord>> map = new HashMap<>();
    private Map<String, CategoryTable> tableMap = new HashMap<>();
    private Set TopCountrySet = new HashSet<String>(
            Arrays.asList(new String[]{"美国", "法国", "英国", "德国", "沙特阿拉伯", "加拿大", "澳大利亚", "意大利", "荷兰", "比利时", "瑞士", "西班牙", "日本", "瑞典", "印度", "中国"}));

    {

        HashSet<String> countries = new HashSet<String>(
                Arrays.asList(new String[]{"美国", "法国", "英国", "德国", "沙特阿拉伯", "加拿大", "澳大利亚", "意大利", "荷兰", "比利时", "瑞士", "西班牙", "日本", "瑞典", "中国", "印度"}));

//        map.put("[学科]国家间合作", (CategoryTable table, WosRecord wosRecord) -> {
//            wosRecord.getSubjectList().forEach((subject) -> {
//                wosRecord.getCountryList().forEach((country) -> {
//                    if (countries.contains(country)) {
//                        table.increase(subject, country);
//                    }
//                });
//            });
//        });
//
//        // ====================================
//        map.put("[学科]中非年度合作篇次", (CategoryTable table, WosRecord wosRecord) -> {
//            wosRecord.getSubjectList().forEach((subject) -> {
//                if (wosRecord.hasChineseAuthor()) {
//                    table.increase(subject, wosRecord.getYear());
//                }
//            });
//        });
//
//        // ====================================
//        map.put("[学科]基金资助", (CategoryTable table, WosRecord wosRecord) -> {
//
//            wosRecord.getSubjectList().forEach((subject) -> {
//                table.increase(subject, "总篇次");
//                if (wosRecord.getFoundList().size() > 0) {
//                    table.increase(subject, "基金资助篇次");
//                    if (wosRecord.hasChineseFund()) {
//                        table.increase(subject, "中国基金资助次");
//                    }
//                }
//            });
//        });
//
//        // ====================================
//        map.put("[学科]非洲各国篇次", (CategoryTable table, WosRecord wosRecord) -> {
//            wosRecord.getSubjectList().forEach((subject) -> {
//                wosRecord.getAfricaCountryList().forEach((country) -> {
//                    table.increase(subject, country);
//                });
//            });
//        });
//
//        // ====================================
//        map.put("[学科]非洲各国篇次", (CategoryTable table, WosRecord wosRecord) -> {
//            wosRecord.getSubjectList().forEach((subject) -> {
//                wosRecord.getAfricaCountryList().forEach((country) -> {
//                    table.increase(subject, country);
//                });
//            });
//        });
//
//        // ====================================
//        map.put("[学科]各国主导信息", (CategoryTable table, WosRecord wosRecord) -> {
//            wosRecord.getSubjectList().forEach((subject) -> {
//                String firstCountry = wosRecord.getFirstCountry();
//                table.increase(subject, firstCountry);
//                table.increase(subject, "all");
//            });
//        });
//
//        // ====================================
//        map.put("[学科]零被引", (CategoryTable table, WosRecord wosRecord) -> {
//            wosRecord.getSubjectList().forEach((subject) -> {
//                table.increase(subject, "总篇次");
//                if (0 == wosRecord.getCitedTimes()) {
//                    table.increase(subject, "零被引篇次");
//                }
//            });
//        });
//
//        // 非洲国家 基金论文数量 中国基金资助数量 中国基金资助占基金资助总论文比例
//
//        map.put("[非洲国家]中国基金资助比例", (CategoryTable table, WosRecord wosRecord) -> {
//            if (wosRecord.getFoundList().size() > 0) {
//                wosRecord.getAfricaCountryList().forEach(country -> {
//                    table.increase(country, "基金论文数量");
//                    if (wosRecord.hasChineseFund()) {
//                        table.increase(country, "中国基金资助数量");
//                    }
//                });
//            }
//        });
//
//        // 年 中国资助的篇次 中国资助的被引次数 资助的篇次 资助的被引
//        map.put("[年]基金资助的被引", (CategoryTable table, WosRecord wosRecord) -> {
//            String year = wosRecord.getYear();
//
//            if (wosRecord.getFoundList().size() > 0) {
//                if (wosRecord.hasChineseFund()) {
//                    table.increase(year, "中国资助的篇次");
//                    table.increase(year, "中国资助的被引次数", wosRecord.getCitedTimes());
//                }
//
//                table.increase(year, "总资助的篇次");
//                table.increase(year, "总资助的被引", wosRecord.getCitedTimes());
//            }
//        });
//
//        // ====================================
//        map.put("[国家]主导篇次、发文篇数信息", (CategoryTable table, WosRecord wosRecord) -> {
//            List<String> countryList = wosRecord.getCountryList();
//            for (int i = 0; i < countryList.size(); i++) {
//                String country = countryList.get(i);
//                if (0 == i) {
//                    table.increase(country, "主导篇次");
//                }
//                table.increase(country, "发文篇数");
//                if (Utils.isAfrica(country)) {
//                    table.increase(country, "非洲", 0);
//                }
//            }
//        });
//
//        // ====================================
//        map.put("3.3.1[合作]非洲、各国之间合作", (CategoryTable table, WosRecord wosRecord) -> {
//            List<String> countryList = wosRecord.getCountryList();
//
//            countryList = countryList.stream().map(country -> {
//                if (Utils.isAfrica(country)) {
//                    return "非洲";
//                }
//                return country;
//            }).distinct().collect(Collectors.toList());
//
//            for (int i = 0; i < countryList.size() - 1; i++) {
//                for (int j = i + 1; j < countryList.size(); j++) {
//                    table.increase(countryList.get(i), countryList.get(j));
//                }
//            }
//        });
//
//        // ====================================
//        map.put("3.2.4[按年被引次数]非洲按年，国际合作+非国际合作(非洲内国家合作及无国家合作)信息", (CategoryTable table, WosRecord wosRecord) -> {
//            List<String> countryList = wosRecord.getCountryList();
//
//            final String year = wosRecord.getYear();
//            final int citedTimes = wosRecord.getCitedTimes();
//
//            if (wosRecord.hasOtherCountry()) {
//                table.increase(year, "国际合作论文数");
//                table.increase(year, "国际合作论文被引次数", citedTimes);
//            } else {
//                table.increase(year, "非国际合作论文数");
//                table.increase(year, "非国际合作论文被引次数", citedTimes);
//            }
//        });
//
//        map.put("3.4.2[按年被引次数 原始数据]非洲按年，中非合作论文，中国主导的论文，非洲的论文， 聊中国外的国际合作", (CategoryTable table, WosRecord wosRecord) -> {
//
//            final String year = wosRecord.getYear();
//            final int citedTimes = wosRecord.getCitedTimes();
//
//            List<String> notAfricaCountryList = wosRecord.getNotAfricaCountryList();
//
//            if (notAfricaCountryList.isEmpty()) {
//                // 无非洲外国家
//                table.increase(String.format("%s\t无国际合作\t%s", wosRecord.getID(), wosRecord.getYear()), "被引", wosRecord.getCitedTimes());
//            } else {
//                if (wosRecord.hasChina()) {
//                    table.increase(String.format("%s\t中非合作\t%s", wosRecord.getID(), wosRecord.getYear()), "被引", wosRecord.getCitedTimes());
//                } else {
//                    table.increase(String.format("%s\t除中国国际合作\t%s", wosRecord.getID(), wosRecord.getYear()), "被引", wosRecord.getCitedTimes());
//                }
//            }
//        });
//
//        // ====================================
//        map.put("3.3.3[合作]非洲外国家按年合作篇次", (CategoryTable table, WosRecord wosRecord) -> {
//            List<String> countryList = wosRecord.getCountryList();
//
//            countryList.stream().forEach(country -> {
//                if (!Utils.isAfrica(country)) {
//                    table.increase(wosRecord.getYear(), country);
//                }
//            });
//        });
//
//        // ====================================
//        map.put("[合作]仅非洲外合作,仅非洲内合作,非洲内外都有合作", (CategoryTable table, WosRecord wosRecord) -> {
//            int citedTimes = wosRecord.getCitedTimes();
//            String year = wosRecord.getYear();
//
//            int africaCount = wosRecord.getAfricaCountryList().size();
//            List<String> countryList = wosRecord.getCountryList();
//            int otherCount = countryList.size() - africaCount;
//
//            String firstCountry = wosRecord.getFirstCountry();
//
//            table.increase(year, "总被引", citedTimes);
//            table.increase(year, "总篇数");
//
//            if (TopCountrySet.contains(firstCountry)) {
//                table.increase(year, firstCountry + "主导被引", citedTimes);
//                table.increase(year, firstCountry + "主导篇数");
//            }
//
//            countryList.forEach(country -> {
//                if (TopCountrySet.contains(country)) {
//                    table.increase(year, country + "合作被引", citedTimes);
//                    table.increase(year, country + "合作篇数");
//                }
//            });
//
//            if (Utils.isAfrica(firstCountry)) {
//                table.increase(year, "非洲主导被引", citedTimes);
//                table.increase(year, "非洲主导篇数");
//            }
//
//            if (countryList.size() > 1) {
//                table.increase(year, "合作被引", citedTimes);
//                table.increase(year, "合作篇数");
//
//                if (0 == otherCount) {
//                    table.increase(year, "仅非洲内合作被引", citedTimes);
//                    table.increase(year, "仅非洲内合作篇数");
//                } else if (1 == africaCount) {
//                    table.increase(year, "仅非洲外合作被引", citedTimes);
//                    table.increase(year, "仅非洲外合作篇数");
//                } else {
//                    table.increase(year, "非洲内外都有合作被引", citedTimes);
//                    table.increase(year, "非洲内外都有合作篇数");
//                }
//
//            } else {
//                table.increase(year, "无合作被引", citedTimes);
//                table.increase(year, "无合作篇数");
//            }
//
//            if (0 == citedTimes) {
//                table.increase(year, "篇数(0被引)");
//
//                if (TopCountrySet.contains(firstCountry)) {
//                    table.increase(year, firstCountry + "主导篇数(0被引)");
//                }
//
//                countryList.forEach(country -> {
//                    if (TopCountrySet.contains(country)) {
//                        table.increase(year, country + "合作篇数(0被引)");
//                    }
//                });
//
//                if (Utils.isAfrica(firstCountry)) {
//                    table.increase(year, "非洲主导篇数(0被引)");
//                }
//
//                if (countryList.size() > 1) {
//                    table.increase(year, "合作篇数(0被引)");
//
//                    if (0 == otherCount) {
//                        table.increase(year, "仅非洲内合作篇数(0被引)");
//                    } else if (1 == africaCount) {
//                        table.increase(year, "仅非洲外合作篇数(0被引)");
//                    } else {
//                        table.increase(year, "非洲内外都有合作篇数(0被引)");
//                    }
//
//                } else {
//                    table.increase(year, "无合作篇数(0被引)");
//                }
//            }
//        });
//
//        map.put("4.1 [主导]非洲各国，主导与非主导", (CategoryTable table, WosRecord wosRecord) -> {
//
//            if (wosRecord.isCoperated()) {
//                List<String> africaCountryList = wosRecord.getAfricaCountryList();
//
//                africaCountryList.forEach(country -> {
//                    table.increase(country, "合作篇数");
//                    table.increase(country, "合作被引", wosRecord.getCitedTimes());
//                });
//
//                if (wosRecord.isFirstAfrica()) {
//                    table.increase(wosRecord.getFirstCountry(), "主导篇数");
//                    table.increase(wosRecord.getFirstCountry(), "主导被引", wosRecord.getCitedTimes());
//                }
//            }
//        });
//
//        map.put("5.2.1[学科]中非合作学科", (CategoryTable table, WosRecord wosRecord) -> {
//            wosRecord.getSubjectList().stream().forEach(subject -> {
//
//                if (wosRecord.hasOtherCountry()) {
//                    if (wosRecord.hasChina()) {
//                        table.increase(subject, "中非合作篇数");
//                        table.increase(subject, "中非合作被引", wosRecord.getCitedTimes());
//                    } else {
//                        table.increase(subject, "除中国国际合作篇数");
//                        table.increase(subject, "除中国国际合作被引", wosRecord.getCitedTimes());
//                    }
//                } else {
//                    table.increase(subject, "无国际合作篇数");
//                    table.increase(subject, "无国际合作被引", wosRecord.getCitedTimes());
//                }
//
//                if (wosRecord.isFirstChina()) {
//                    table.increase(subject, "中国主导篇数");
//                    table.increase(subject, "中国主导被引", wosRecord.getCitedTimes());
//                }
//            });
//        });
//
//        map.put("3.4.3 中国与非洲各国合作情况", (CategoryTable table, WosRecord wosRecord) -> {
//            if (wosRecord.hasChina()) {
//                wosRecord.getCountryList().stream().forEach(country -> {
//                    if (Utils.isAfrica(country)) {
//                        table.increase(country, "与中国合作次数");
//                    }
//                });
//            }
//        });
//
//        map.put("图 3 1 1 非洲地区论文产量年度分布", (CategoryTable table, WosRecord wosRecord) -> {
//            table.increase(wosRecord.getYear(), "篇次");
//        });
//
//        map.put("图 3 2 1 非洲地区论文总量、合作论文数量及合作率", (CategoryTable table, WosRecord wosRecord) -> {
//            int countryCount = wosRecord.getCountryList().size();
//
//            table.increase("非洲", "论文总篇数");
//            if (countryCount > 1) {
//                table.increase("非洲", "合作论文篇数");
//            }
//
//            wosRecord.getAfricaCountryList().forEach(country -> {
//                table.increase(country, "论文总篇数");
//
//                if (countryCount > 1) {
//                    table.increase(country, "合作论文篇数");
//                }
//            });
//        });
//
//        map.put("3.2.3 非洲各国论文合作情况", (table, wosRecord) -> {
//
//            List<String> countryList = wosRecord.getCountryList();
//            List<String> africaCountryList = wosRecord.getAfricaCountryList();
//            List<String> otherCountryList = countryList.stream().filter(country -> !Utils.isAfrica(country)).collect(Collectors.toList());
//
//            africaCountryList.forEach(africaCountry -> {
//                if (countryList.size() > 1) {
//                    table.increase(africaCountry, "合作");
//                    table.increase(africaCountry, "合作被引", wosRecord.getCitedTimes());
//
//                    if (0 == otherCountryList.size()) {
//                        table.increase(africaCountry, "仅非洲内合作");
//                        table.increase(africaCountry, "仅非洲内合作被引", wosRecord.getCitedTimes());
//                    } else if (1 == africaCountryList.size()) {
//                        table.increase(africaCountry, "仅非洲外合作");
//                        table.increase(africaCountry, "仅非洲外合作被引", wosRecord.getCitedTimes());
//                    } else {
//                        table.increase(africaCountry, "非洲内外都有合作");
//                        table.increase(africaCountry, "非洲内外都有合作被引", wosRecord.getCitedTimes());
//                    }
//
//                    if (otherCountryList.contains("中国")) {
//                        table.increase(africaCountry, "中国合作");
//                        table.increase(africaCountry, "中国合作被引", wosRecord.getCitedTimes());
//                    }
//                } else {
//                    table.increase(africaCountry, "无合作");
//                    table.increase(africaCountry, "无合作被引", wosRecord.getCitedTimes());
//                }
//            });
//        });
//
//        map.put("6.1.2 非洲各国基金论文分析", (table, wosRecord) -> {
//            // 非洲各国每年的基金论文数
//            if (wosRecord.hasFound()) {
//                wosRecord.getCountryList().forEach(country -> {
//                    table.increase(wosRecord.getYear(), country);
//                });
//            }
//        });
//
//        map.put("6.3.3 中国主要基金资助论文影响力", (table, wosRecord) -> {
//
//            String year = wosRecord.getYear();
//            int citedTimes = wosRecord.getCitedTimes();
//
//            table.increase(year, "总篇数");
//            table.increase(year, "总被引", citedTimes);
//
//            if (wosRecord.hasFound()) {
//                table.increase(year, "基金资助篇数");
//                table.increase(year, "基金资助被引", citedTimes);
//                if (wosRecord.hasChineseFund()) {
//                    table.increase(year, "中国基金资助论文篇数");
//                    table.increase(year, "中国基金资助论文被引", citedTimes);
//                }
//            }
//        });
//
//        map.put("4 2 合作国家主导篇次及主导率", (table, wosRecord) -> {
//            wosRecord.getCountryList().stream().forEach(country -> {
//                if (!Utils.isAfrica(country)) {
//                    table.increase(country, "合作频次");
//                    table.increase(country, "合作被引", wosRecord.getCitedTimes());
//                }
//            });
//
//            String firstCountry = wosRecord.getFirstCountry();
//            if (!Utils.isAfrica(firstCountry)) {
//                table.increase(firstCountry, "主导篇次");
//                table.increase(firstCountry, "主导被引", wosRecord.getCitedTimes());
//            }
//        });
//
//        map.put("5 3：发文量前16个学科产出及合作情况", (table, wosRecord) -> {
//
//            wosRecord.getSubjectList().stream().forEach(subject -> {
//
//                table.increase(subject, "发文总篇次");
//                table.increase(subject, "发文总被引", wosRecord.getCitedTimes());
//
//                if (wosRecord.hasOtherCountry()) {
//                    table.increase(subject, "国际合作频次");
//                    table.increase(subject, "国际合作被引", wosRecord.getCitedTimes());
//
//                    wosRecord.getNotAfricaCountryList().stream().forEach(country -> {
//                        table.increase(subject, "国际合作国家数量", country);
//                    });
//                }
//            });
//        });
//
//        map.put("6.3.1 有无基金资助论文平均被引次数", (table, wosRecord) -> {
//
//            int citedTimes = wosRecord.getCitedTimes();
//
//            table.increase(wosRecord.getYear(), "论文");
//            table.increase(wosRecord.getYear(), "论文被引", citedTimes);
//
//            if (wosRecord.hasFound()) {
//                table.increase(wosRecord.getYear(), "基金论文");
//                table.increase(wosRecord.getYear(), "基金论文被引", citedTimes);
//            } else {
//                table.increase(wosRecord.getYear(), "无基金论文");
//                table.increase(wosRecord.getYear(), "无基金论文被引", citedTimes);
//            }
//
//            if (0 == citedTimes) {
//                table.increase(wosRecord.getYear(), "0被引论文");
//
//                if (wosRecord.hasFound()) {
//                    table.increase(wosRecord.getYear(), "0被引基金论文");
//                } else {
//                    table.increase(wosRecord.getYear(), "0被引无基金论文");
//                }
//            }
//        });
//
//        map.put("4.1.1 非洲地区论文主导率", (table, wosRecord) -> {
//
//            table.increase(wosRecord.getYear(), "总篇数");
//
//            if (Utils.isAfrica(wosRecord.getFirstCountry())) {
//                table.increase(wosRecord.getYear(), "主导篇数");
//            }
//        });
//
//        map.put("6.3.3 中国主要基金资助论文的数量及被引情况", (table, wosRecord) -> {
//            if (wosRecord.hasChineseFund()) {
//                table.increase(wosRecord.getYear(), "基金论文数");
//                table.increase(wosRecord.getYear(), "基金论文总被引次数", wosRecord.getCitedTimes());
//
//                if (0 == wosRecord.getCitedTimes()) {
//                    table.increase(wosRecord.getYear(), "零被引基金论文数");
//                }
//            }
//        });
//
//        HashSet set = new HashSet();
//        set.add("美国");
//        set.add("法国");
//        set.add("英国");
//        set.add("德国");
//        set.add("沙特阿拉伯");
//        set.add("加拿大");
//        set.add("澳大利亚");
//        set.add("意大利");
//        set.add("荷兰");
//        set.add("比利时");
//        set.add("瑞士");
//        set.add("西班牙");
//        set.add("日本");
//        set.add("中国");
//        set.add("瑞典");
//        set.add("印度");
//        set.add("丹麦");
//        set.add("巴西");
//        set.add("挪威");
//        set.add("奥地利");
//        set.add("葡萄牙");
//        set.add("波兰");
//        set.add("巴布亚新几内亚");
//        set.add("俄罗斯联邦");
//        set.add("土耳其");
//        set.add("马来西亚");
//        set.add("韩国");
//        set.add("以色列");
//        set.add("芬兰");
//        set.add("捷克");
//        set.add("新西兰");
//        set.add("希腊");
//
//        map.put("[合作] 非洲与其它国家之间", (table, wosRecord) -> {
//            wosRecord.getNotAfricaCountryList().forEach(country -> {
//                table.increase(country, "非洲");
//            });
//        });
//
//        map.put("[合作] 中国与非洲各国", (table, wosRecord) -> {
//            if (wosRecord.hasChina()) {
//                wosRecord.getAfricaCountryList().forEach(country -> {
//                    table.increase(country, "中国");
//                });
//            }
//        });
//
//
//        map.put("[合作网络] 非洲与其它国家之间", (table, wosRecord) -> {
//
//            List<String> list = wosRecord.getCountryList().stream().filter(country -> {
//                return set.contains(country);
//            }).collect(Collectors.toList());
//
//            list.add("非洲");
//
//            list.stream().forEach(self -> {
//                list.stream().forEach(other -> {
//                    if (!other.equals(self)) {
//                        table.increase(self, other);
//                    }
//                });
//            });
//        });
//
//        map.put("[合作网络] 中国与非洲各国", (table, wosRecord) -> {
//            if (wosRecord.hasChina()) {
//                List<String> list = wosRecord.getAfricaCountryList();
//                list.add("中国");
//
//                list.stream().forEach(self -> {
//                    list.stream().forEach(other -> {
//                        if (!other.equals(self)) {
//                            table.increase(self, other);
//                        }
//                    });
//                });
//            }
//        });
//
//        map.put("[非洲 主导 年]非洲地区主导论文影响力", (table, wosRecord) -> {
//
//            String firstCountry = wosRecord.getFirstCountry();
//            String year = wosRecord.getYear();
//            if (Utils.isAfrica(firstCountry)) {
//                table.increase(year, "主导篇数");
//                table.increase(year, "主导被引", wosRecord.getCitedTimes());
//            } else {
//                table.increase(year, "非主导篇数");
//                table.increase(year, "非主导被引", wosRecord.getCitedTimes());
//            }
//        });
//
//        Set<String> topSubjectSet = new HashSet<>();
//        topSubjectSet.add("农学");
//        topSubjectSet.add("化学");
//        topSubjectSet.add("地质学");
//        topSubjectSet.add("天文学与天体物理学");
//        topSubjectSet.add("工程学");
//        topSubjectSet.add("数学");
//        topSubjectSet.add("材料科学");
//        topSubjectSet.add("植物学");
//        topSubjectSet.add("物理学");
//        topSubjectSet.add("环境科学与生态学");
//        topSubjectSet.add("生物化学与分子生物学");
//        topSubjectSet.add("科学技术及其他");
//        topSubjectSet.add("药理学及药剂学");
//        topSubjectSet.add("计算机科学");
//
//        map.put("[学科影响力]中非 原始", (table, wosRecord) -> {
//            List<String> notAfricaCountryList = wosRecord.getNotAfricaCountryList();
//            wosRecord.getSubjectList().stream().filter(topSubjectSet::contains).forEach(subject -> {
//                if (notAfricaCountryList.isEmpty()) {
//                    //                    // 无非洲外国家
//                    table.increase(String.format("%s\t无国际合作\t%s", wosRecord.getID(), subject), "被引", wosRecord.getCitedTimes());
//                } else {
//                    if (wosRecord.hasChina()) {
//                        table.increase(String.format("%s\t中非合作\t%s", wosRecord.getID(), subject), "被引", wosRecord.getCitedTimes());
//                    } else {
//                        table.increase(String.format("%s\t除中国国际合作\t%s", wosRecord.getID(), subject), "被引", wosRecord.getCitedTimes());
//                    }
//                }
//            });
//        });
//
//        map.put("[学科影响力]中非 统计", (table, wosRecord) -> {
//            List<String> notAfricaCountryList = wosRecord.getNotAfricaCountryList();
//            wosRecord.getSubjectList().forEach(subject -> {
//                if (notAfricaCountryList.isEmpty()) {
//                    // 无非洲外国家
//                    table.increase(subject, "无非洲外国家篇次");
//                    table.increase(subject, "无非洲外国家被引", wosRecord.getCitedTimes());
//                } else {
//                    if (wosRecord.hasChina()) {
//                        table.increase(subject, "中非合作篇次");
//                        table.increase(subject, "中非合作被引", wosRecord.getCitedTimes());
//                    } else {
//                        table.increase(subject, "除中国国际合作篇次");
//                        table.increase(subject, "除中国国际合作被引", wosRecord.getCitedTimes());
//                    }
//                }
//            });
//        });
//
//        map.put("4.1.1 非洲地区论文主导率年度分布", (CategoryTable table, WosRecord wosRecord) -> {
//            if (wosRecord.isCoperated()) {
//                table.increase(wosRecord.getYear(), "合作篇数");
//                table.increase(wosRecord.getYear(), "合作被引", wosRecord.getCitedTimes());
//
//                if (wosRecord.isFirstAfrica()) {
//                    table.increase(wosRecord.getYear(), "非洲主导篇数");
//                    table.increase(wosRecord.getYear(), "非洲主导被引", wosRecord.getCitedTimes());
//                } else {
//                    table.increase(wosRecord.getYear(), "非非洲主导篇数");
//                    table.increase(wosRecord.getYear(), "非非洲主导被引", wosRecord.getCitedTimes());
//                }
//            }
//        });
//
//        map.put("4.1.2 非洲各国论文主导情况", (CategoryTable table, WosRecord wosRecord) -> {
//            if (wosRecord.isCoperated()) {
//                wosRecord.getAfricaCountryList().forEach(country -> {
//                    table.increase(country, "合作篇数");
//                    table.increase(country, "合作被引", wosRecord.getCitedTimes());
//
//                    if (wosRecord.isFirstAfrica()) {
//                        table.increase(country, "主导篇数");
//                        table.increase(country, "主导被引", wosRecord.getCitedTimes());
//                    } else {
//                        table.increase(country, "非主导篇数");
//                        table.increase(country, "非主导被引", wosRecord.getCitedTimes());
//                    }
//                });
//            }
//        });
//        map.put("4.3.2 中国主导论文质量", (CategoryTable table, WosRecord wosRecord) -> {
//            if (wosRecord.isCoperated() && wosRecord.hasChina()) {
//                if (wosRecord.isFirstChina()) {
//                    table.increase(wosRecord.getYear(), "中国主导篇数");
//                    table.increase(wosRecord.getYear(), "中国主导被引", wosRecord.getCitedTimes());
//                } else {
//                    if (wosRecord.isFirstAfrica()) {
//                        table.increase(wosRecord.getYear(), "非洲主导篇数");
//                        table.increase(wosRecord.getYear(), "非洲主导被引", wosRecord.getCitedTimes());
//                    }
//                    table.increase(wosRecord.getYear(), "非中国主导篇数");
//                    table.increase(wosRecord.getYear(), "非中国主导被引", wosRecord.getCitedTimes());
//                }
//            }
//        });
//
//        map.put("4.1.3 非洲地区主导论文影响力", (table, wosRecord) -> {
//            if (wosRecord.isCoperated()) {
//                if (wosRecord.isFirstAfrica()) {
//                    table.increase(wosRecord.getYear(), "非洲主导篇数");
//                    table.increase(wosRecord.getYear(), "非洲主导被引", wosRecord.getCitedTimes());
//                } else {
//                    table.increase(wosRecord.getYear(), "非非洲主导篇数");
//                    table.increase(wosRecord.getYear(), "非非洲主导被引", wosRecord.getCitedTimes());
//                }
//            }
//        });
//
//        map.put("4.2.1. 合作国家主导率", (table, wosRecord) -> {
//            if (wosRecord.isCoperated()) {
//                wosRecord.getNotAfricaCountryList().forEach(country -> {
//                    table.increase(country, "合作频次");
//                });
//
//                String firstCountry = wosRecord.getFirstCountry();
//                if (!Utils.isAfrica(firstCountry)) {
//                    table.increase(firstCountry, "主导篇数");
//                }
//            }
//        });

        map.put("5.1.3. 非洲地区各学科合作论文主导情况分析", (table, wosRecord) -> {
            if (wosRecord.isCoperated()) {
                boolean firstAfrica = wosRecord.isFirstAfrica();
                wosRecord.getSubjectList().forEach(subject -> {
                    if (firstAfrica) {
                        table.increase(subject, "非洲主导");
                    }
                    table.increase(subject, "合作篇次");
                });
            }
        });
        //====================
    }
}
