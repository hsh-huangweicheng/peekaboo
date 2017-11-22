package utils;

import consts.RegConsts;
import wos.WosRecord;

public class FundUtils {

	public static boolean isChineseFundOrInst(String str) {
		return RegUtils.matchRegPair(str, RegConsts.chineseFundOrInstRegPair);
	}

	public static boolean hasChineseFund(WosRecord wosRecord) {
		return wosRecord.getFoundList().parallelStream().filter(fund -> {
			return isChineseFundOrInst(fund);
		}).findAny().isPresent();
	}

	public static String removeFundNumber(String line) {
		int index = line.lastIndexOf("[");
		String fund = "";
		if (index > 0) {
			fund = line.substring(0, index).trim();
		} else {
			fund = line.replaceAll("[\\[\\]]", "").trim();
		}
		return fund;
	}

	public static String normalize(String content) {
		return content.replaceAll("\\W", " ").replaceAll("\\s+", " ").toUpperCase().trim();
	}

	public static boolean hasChineseFundInFX(String fx) {
		String normalizeFX = normalize(fx);

		return ResourceUtils.getChineseFundList().parallelStream().filter(fund -> {
			return normalizeFX.indexOf(fund) >= 0;
		}).findAny().isPresent();
	}
}
