package wos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import consts.RegConsts;
import utils.CaseUtils;
import utils.RegUtils;

public class WosRecord {

	private Map<String, List<String>> map = new HashMap<>();

	private Pattern lastWordPattern = Pattern.compile("([ \\w]+)\\.?$");

	private String path;

	public WosRecord() {
	}

	public void addField(String fieldName, List<String> lines) {
		this.map.put(fieldName, lines);
	}

	public String getString(String fieldName) {
		List<String> list = this.getList(fieldName);
		return StringUtils.join(list, " ");
	}

	public List<String> getList(String fieldName) {
		List<String> fieldValueList = map.get(fieldName);
		if (null == fieldValueList) {
			return new ArrayList<>(0);
		} else {
			return fieldValueList;
		}
	}

	public List<String> getCountryList() {
		List<String> list = new ArrayList<>();

		list.addAll(this.getList("C1"));
		list.addAll(this.getList("RP"));

		return list.stream().map((line) -> {
			String country = RegUtils.getMatchedKey(line, RegConsts.countryRegPairList);

			if (StringUtils.isEmpty(country)) {
				Matcher matcher = lastWordPattern.matcher(line);
				if (matcher.find()) {
					country = matcher.group(1).trim();
					country = CaseUtils.convertCamelCase(country);
				}
			}

			return country;
		}).distinct().collect(Collectors.toList());

	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}
}
