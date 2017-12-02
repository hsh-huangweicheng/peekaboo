package analyzer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class FXAnalyzerTest {

	@Test
	public void test1() {

		NlpFXAnalyzer fxAnalyzer = new NlpFXAnalyzer(null);
		List<String[]> list = new ArrayList<>();
		list.add(new String[] { "[R01DC008333]", "R01DC008333" });
		list.add(new String[] { "xx R01DC008333]", "R01DC008333" });
		list.add(new String[] { "xx R01DC-008333]", "R01DC-008333" });
		list.add(new String[] { "xx R01DC.]", "R01DC." });
		list.add(new String[] { "xx R01/02DC ]", "R01/02DC" });
		list.add(new String[] { "xx RDDDDC ]", "" });
		list.add(new String[] { "xx RDD2DDC ]", "RDD2DDC" });
		list.add(new String[] { "xx RDD-DDC ]", "" });

		list.stream().forEach((strs) -> {
			String identityOfText = fxAnalyzer.getIdentityOfText(strs[0]);
			assertEquals(strs[0] + "==" + strs[1], strs[1], identityOfText);
		});

	}
}
