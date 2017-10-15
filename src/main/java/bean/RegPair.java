package bean;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RegPair {

	private String name;

	private List<Pattern> patternList;

	private Pattern pattern;

	public RegPair(String name, String[] regs) {
		this.name = name;
		this.patternList = toPatterns(regs);
	}

	public RegPair(String name, String reg, String[] regs) {
		this.name = name;
		this.pattern = toPatterns(new String[] { reg }).get(0);
		this.patternList = toPatterns(regs);
	}

	public String getName() {
		return name;
	}

	public Pattern getPattern() {
		return pattern;
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
