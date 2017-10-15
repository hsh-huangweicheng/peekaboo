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
		countryRegPairList.add(new RegPair("England", new String[] { "England", "Scotland", "North Ireland", "U.K" }));
		countryRegPairList.add(new RegPair("Saint Kitts and Nevis", new String[] { "St Kitts & Nevi", "Saint Kitts and Nevis", "St. Kitts and Nevis" }));
		countryRegPairList.add(new RegPair("China", new String[] { "China" }));
		countryRegPairList.add(new RegPair("UAE", new String[] { "U Arab Emirates", "UAE" }));

		countryRegPairList.add(new RegPair("Rep Of Congo", "Congo", new String[] { "Brazzaville", "Brazaville", "Mossaka", "Pointe, Noire", "Pointe Noire",
				"Kisangani", "LIKOUALA", "Owando", "Sibiti", "Loutété", "Dolisie", "Nkayi", "Impfondo", "Ouésso", "Madingou", "Benin" }));

		countryRegPairList.add(new RegPair("Rep Dem Congo", "Congo",
				new String[] { "Bunia", "Kalemie", "Ituri Forest", "Ngungu", "Mabali", "Maniema", "Uvira", "Congolese", "Lubuntu", "Kivu", "Nduye", "Nyankunde",
						"Epulu", "Kinshasa", "Kimpese", "Lwiro", "Bukavu", "Bwamanda", "Goma", "Lubumbashi", "Watsa", "Kinhasa", "Kinshasha", "Kikwit", "Nioki",
						"Mbujimayi", "Ituru", "Kananga", "Kisangani", "Kidu", "Matadi", "Mbandaka", "Likasi", "Mbuji Mayi", "Tshikapa", "Masina", "Kolwezi" }));

		countryRegPairList.add(new RegPair("Sudan", "Sudan", new String[] { "Khartoum" }));

		countryRegPairList.add(new RegPair("South Sudan", "Sudan",
				new String[] { "South Sudan", "Juba", "Winejok", "Malakal", "Wau", "Pajok", "Yei", "Yambio", "Aweil", "Gogrial", "Rumbek" }));

		countryRegPairList.add(new RegPair("South Africa", new String[] { "Limpopo Province" }));

		countryRegPairList.add(new RegPair("Israel", new String[] { "Israel" }));

	}

}
