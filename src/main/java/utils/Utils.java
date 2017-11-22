package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Utils {

	private static List<int[]> list = new ArrayList<>();

	static {
		list.add(new int[] { 1977, 1992 });
		list.add(new int[] { 1992, 2000 });
		list.add(new int[] { 2000, 2010 });
		list.add(new int[] { 2010, 2017 });
	}

	public static String getYearGroup(String _year) {

		int year = Integer.parseInt(_year);

		Optional<int[]> findAny = list.stream().filter((int[] years) -> {
			return years[0] <= year && year < years[1];
		}).findAny();

		if (findAny.isPresent()) {
			int[] years = findAny.get();

			return years[0] + "-" + years[1];
		}

		System.err.println("can not find year:" + _year);
		return "";
	}
}
