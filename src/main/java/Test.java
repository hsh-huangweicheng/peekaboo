import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws IOException {


		String str = "National Institutes of Health [R01DC008333, R01DC013315]";


		System.out.println(Arrays.asList(str.split("[\\[\\]\\(\\)]")));
	}

	public static void convertCamelCase(String text) {
		Pattern pattern = Pattern.compile("");

		String str = "National Institutes of Health [R01DC008333, R01DC013315];";

		Matcher matcher = pattern.matcher(str);
		List<String> list = new ArrayList<>();
		while (matcher.find()) {
			int groupCount = matcher.groupCount();
			for (int i = 0; i < groupCount; i++) {
				list.add(matcher.group(i));
			}
		}

		System.out.println(list.size() + "-" + list);
	}
}
