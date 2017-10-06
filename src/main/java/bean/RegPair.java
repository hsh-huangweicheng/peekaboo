package bean;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegPair {

	private String name;

	private List<Pattern> patternList;

	public RegPair(String name, String[] regs) {
		this.name = name;
		this.patternList = toPatterns(regs);
	}

	public String getName() {
		return name;
	}

	private List<Pattern> toPatterns(String[] strs) {
		return Arrays.asList(strs).stream().map(str -> {
			Pattern pattern = Pattern.compile("\\$$");
			if (pattern.matcher(str).find()) {
				return "\\b" + str;
			} else {
				return "\\b" + str + "\\b";
			}

		}).map(str -> Pattern.compile(str, Pattern.CASE_INSENSITIVE)).collect(Collectors.toList());
	}

	public List<Pattern> getPatternList() {
		return patternList;
	}

}
