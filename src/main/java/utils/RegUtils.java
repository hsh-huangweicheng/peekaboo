package utils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import bean.RegPair;

public class RegUtils {

	public static String getMatchedKey(String text, List<RegPair> regPairList) {

		Optional<RegPair> findFirst = regPairList.stream().filter(regPair -> {

			Pattern prePattern = regPair.getPattern();
			if (null != prePattern) {
				if (!prePattern.matcher(text).find()) {
					return false;
				}
			}

			Optional<Pattern> findAny = regPair.getPatternList().stream().filter((pattern) -> {
				return pattern.matcher(text).find();
			}).findAny();
			return findAny.isPresent();

		}).findFirst();

		if (findFirst.isPresent()) {
			return findFirst.get().getName();
		}

		return "";
	}

}
