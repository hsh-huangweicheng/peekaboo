package consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bean.RegPair;

public abstract class RegConsts {

	public static List<RegPair> countryRegPairList = new ArrayList<>();

	static {

		List<String> list = Arrays.asList(new String[] { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS", "KY",
				"LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC", "SD",
				"TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY", "PR", "Gu", "Ap", "DC" });

		list = list.stream().map(str -> str + "( \\d{5})?\\.?$").collect(Collectors.toList());
		list.add(0, "USA");

		countryRegPairList.add(new RegPair("USA", (String[]) list.toArray(new String[list.size()])));
		countryRegPairList.add(new RegPair("England", new String[] { "England", "Scotland", "North Ireland" }));
		countryRegPairList.add(new RegPair("Saint Kitts and Nevis", new String[] { "St Kitts & Nevi", "Saint Kitts and Nevis", "St. Kitts and Nevis" }));
		countryRegPairList.add(new RegPair("China", new String[] { "China" }));

	}

}
