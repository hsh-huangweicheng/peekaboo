package wos;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
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
import utils.ResourceUtils;

public class WosRecord {

	private Map<String, List<String>> map = new HashMap<>();

	private Pattern lastWordPattern = Pattern.compile("([& \\w]+)\\.?$");

	private String path;

	private List<String> countryList;

	private Boolean hasChineseFund = null;

	private Boolean hasChineseAuthor = null;
	
	private Boolean hasChineseInst = null;

	public WosRecord() {
	}

	public void addField(String fieldName, List<String> lines) {
		this.map.put(fieldName, lines);
	}

	public String getString(String fieldName) {
		List<String> list = this.getList(fieldName);
		return StringUtils.join(list, " ").trim();
	}

	public List<String> getList(String fieldName) {
		List<String> fieldValueList = map.get(fieldName);
		if (null == fieldValueList) {
			return new ArrayList<>(0);
		} else {
			return fieldValueList;
		}
	}

	/**
	 * 基金机构
	 */
	public List<String> getFoundList() {
		String foundStr = getString("FU");
		if (foundStr.length() > 0) {

			String[] founds = foundStr.split(";");

			return Arrays.asList(founds);
		} else {
			return new ArrayList<>();
		}
	}

	public boolean hasChineseFund() {
		if (null == hasChineseFund) {
			boolean present = this.getFoundList().stream().filter(fund -> {
				return RegUtils.matchRegPair(fund, RegConsts.chineseFundOrInstRegPair);
			}).findAny().isPresent();

			hasChineseFund = new Boolean(present);
		}

		return hasChineseFund.booleanValue();
	}

	public boolean hasChineseAuthor() {
		if (null == hasChineseAuthor) {
			boolean present = this.getCountryList().parallelStream().filter(country -> {
				return "中国".equals(country);
			}).findAny().isPresent();

			hasChineseAuthor = new Boolean(present);
		}

		return hasChineseAuthor.booleanValue();
	}

	public String getYear() {
		return this.getString("PY");
	}

	public List<String> getAfricaCountryList() {
		return this.getCountryList().stream().filter(country -> {
			return ResourceUtils.isAfrica(country);
		}).collect(Collectors.toList());
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

	public int getCitedTimes() {
		String citedTimes = this.getString("TC");
		try {
			return Integer.parseInt(citedTimes, 10);
		} catch (NumberFormatException exception) {
			return 0;
		}
	}

	/**
	 * 从C1或RP中解析出国家名称
	 * 
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

	public String getFirstCountry() {
		List<String> countryList = this.getCountryList();
		if (countryList.isEmpty()) {
			System.err.println("no country UT:" + this.getString("UT"));
			return "";
		}
		return countryList.get(0);
	}
}
