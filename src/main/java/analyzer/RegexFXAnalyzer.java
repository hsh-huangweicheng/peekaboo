package analyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import table.NormalTable;
import table.Table;
import wos.WosRecord;

public class RegexFXAnalyzer implements Analyzer {

	private NormalTable table = new NormalTable("FU", new String[] { "FU" });
	private Pattern compile = Pattern.compile("\\w+ ([A-Z]\\w+[\\w-& ']*)");

	@Override
	public void scan(WosRecord wosRecord) {

		String fx = wosRecord.getString("FX");
		String ut = wosRecord.getString("UT");

		if (StringUtils.isNotBlank(fx)) {
			Matcher matcher = compile.matcher(fx);
			while (matcher.find()) {
				String group = matcher.group(1);

				if (Pattern.compile("(^[A-Z][a-z]+$)").matcher(group).find()) {

				} else {
					if (Pattern.compile("([A-Z]\\w+$)").matcher(group).find()) {
						table.add(ut, group);
					}
				}

			}
		}

	}

	@Override
	public Table[] getTables() {
		return new Table[] { table };
	}

	@Override
	public String getName() {
		return "RegexFXAnalyzer";
	}

}
