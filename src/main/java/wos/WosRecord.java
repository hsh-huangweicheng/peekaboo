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
import utils.ResourceUtils;
import utils.RegUtils;

public class WosRecord {

	private Map<String, List<String>> map = new HashMap<>();

	private Pattern lastWordPattern = Pattern.compile("([& \\w]+)\\.?$");

	private String path;

	private List<String> countryList;

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

		if (null == countryList) {

			List<String> list = new ArrayList<>();

			list.addAll(this.getList("RP"));
			list.addAll(this.getList("C1"));

			countryList = list.parallelStream().map((line) -> {
				return parseCountryFromLine(line);
			}).distinct().collect(Collectors.toList());
		}

		return countryList;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	/**
	 * 从C1或RP中解析出国家名称
	 * @param line
	 * @return
	 */
	private String parseCountryFromLine(String line) {
		String country = RegUtils.getMatchedKey(line, RegConsts.countryRegPairList);

		if (StringUtils.isEmpty(country)) {
			line = line.replaceAll("\\W+$", "");
			Matcher matcher = lastWordPattern.matcher(line);
			if (matcher.find()) {
				country = matcher.group(1).trim();
				country = CaseUtils.convertCamelCase(country);
			}
		}

		String countryName = ResourceUtils.getCountryName(country);

		if (StringUtils.isEmpty(countryName)) {
			System.err.println("[Country Error | " + country + "]\t" + line);
			return country;
		}

		return countryName;
	}
}
