import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

public class Test {

	private static Logger LOG = null;

	// 正常的日期格式
	public static final String DATE_PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";

	// 不带符号的日期格式，用来记录时间戳
	public static final String DATE_PATTERN_NOMARK = "yyyyMMddHHmmss";

	public static void main(String[] args) {
		System.setProperty("java.util.logging.config.file", "logging.properties");
		LOG = Logger.getLogger("com.lujinhong");
		LOG.warning("测试信息");
		LOG.info("hello");
	}
}
