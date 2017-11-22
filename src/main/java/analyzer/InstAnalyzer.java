package analyzer;

import table.CategoryTable;
import table.StatisticTable;
import table.Table;
import utils.FundUtils;
import wos.WosRecord;

public class InstAnalyzer implements Analyzer {

	private StatisticTable chineseInstTable = new StatisticTable("中国机构", new String[] { "简称", "全称" });

	@Override
	public void scan(WosRecord wosRecord) {

		int year = Integer.parseInt(wosRecord.getYear());
		if (1977 <= year && year <= 2016) {

			chineseInst(wosRecord);
		}

	}

	private void chineseInst(WosRecord wosRecord) {

		wosRecord.getList("C1").parallelStream().forEach(line -> {

			if (FundUtils.isChineseFundOrInst(line)) {

				int indexOfChar = endIndexOf(line, "]");
				if (indexOfChar < 0) {
					indexOfChar = endIndexOf(line, "(reprint author)");
				}

				boolean isAccurate = indexOfChar >= 0;
				if (isAccurate) {
					line = line.substring(indexOfChar + 1).trim();
				}

				String inst = line;
				int commaIndex = endIndexOf(line, ",");
				if (commaIndex > 0) {
					inst = line.substring(0, commaIndex - 1);
				}

				chineseInstTable.add(inst).add(line).increase();
			}
		});
	}

	private int endIndexOf(String str, String chr) {
		int indexOfChar = str.indexOf(chr);
		if (indexOfChar >= 0) {
			indexOfChar = indexOfChar + chr.length();
		}
		return indexOfChar;
	}

	@Override
	public Table[] getTables() {
		return new Table[] { chineseInstTable };
	}

	@Override
	public String getName() {
		return "";
	}

}
