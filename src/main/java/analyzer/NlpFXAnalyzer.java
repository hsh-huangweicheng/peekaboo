package analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import bean.NamedEntity;
import bean.NamedEntityType;
import table.StatisticTable;
import utils.NlpUtils;
import wos.WosRecord;

public class NlpFXAnalyzer implements Analyzer {

	private AtomicInteger count = new AtomicInteger(0);

	private StatisticTable table;

	public NlpFXAnalyzer() {
		table = new StatisticTable("default", new String[] { "UT", "TYPE", "ENTITY", "IDENDITY", "COUNT" });
	}

	@Override
	public void scan(WosRecord wosRecord) {

		String utValue = wosRecord.getString("UT");
		String fxValue = wosRecord.getString("FX");

		if (!StringUtils.isEmpty(fxValue)) {

			long timestamp = System.currentTimeMillis();

			List<NamedEntity> namedEntityList = NlpUtils.getNamedEntityList(fxValue);
			List<String> identityOfNamedEntity = getIdentityOfNamedEntity(namedEntityList, fxValue);

			int size = namedEntityList.size();
			for (int i = 0; i < size; i++) {
				NamedEntity namedEntity = namedEntityList.get(i);
				String identity = identityOfNamedEntity.get(i);
				table.add(utValue).add(namedEntity.type).add(namedEntity.name).add(identity);
			}

			System.out.printf("%s %s find %s entities, use [%s] milliseconds\r\n", count.incrementAndGet(), utValue, size,
					System.currentTimeMillis() - timestamp);
		}
	}

	@Override
	public StatisticTable[] getTables() {
		return new StatisticTable[] { table };
	}

	@Override
	public String getName() {
		return "FX";
	}

	private List<String> getIdentityOfNamedEntity(List<NamedEntity> namedEntityList, String text) {
		int size = namedEntityList.size();
		List<String> identityList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			NamedEntity namedEntity = namedEntityList.get(i);
			final int beginPosition = namedEntity.getBeginPosition();
			int endPosition = text.length();
			if (i < (size - 1)) {
				NamedEntity nextEntity = namedEntityList.get(i + 1);
				endPosition = nextEntity.getBeginPosition();
			}

			String identityOfText = "";
			if (namedEntity.type.equals(NamedEntityType.ORGANIZATION)) {
				identityOfText = getIdentityOfText(text.substring(beginPosition, endPosition));
			}
			identityList.add(identityOfText);
		}
		return identityList;
	}

	public String getIdentityOfText(String text) {
		Pattern pattern = Pattern.compile("\\b(?![.A-Z/-]+\\b)([.A-Z/\\d-]{4,})[ \\]\\)\\}]");
		Matcher matcher = pattern.matcher(text);
		while (matcher.find()) {
			return matcher.group(1);
		}
		return "";
	}

}
