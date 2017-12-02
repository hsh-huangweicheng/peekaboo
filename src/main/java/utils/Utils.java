package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import bean.RegPair;
import wos.WosRecord;

public class Utils {

	private static List<int[]> groupYearlist = new ArrayList<>();

	static Properties countryProps = new Properties();

	private static Set<String> africaSet = new HashSet<>();

	private static List<String> chineseFundList = new ArrayList<>();

	public static RegPair chineseFundOrInstRegPair = new RegPair("China", new String[] { "PRC", "china", "chinese", "BeiJing", "ShangHai", "TianJin",
			"ChongQing", "AnHui", "FuJian", "GuangDong", "GuangXi", "GuiZhou", "GanSu", "HaiNan", "HeBei", "HeNan", "HeiLongJiang", "HuBei", "HuNan", "JiLin",
			"JiangSu", "JiangXi", "LiaoNing", "NeiMengGu", "NingXia", "QingHai", "ShanXi", "ShanXi", "ShanDong", "SiChuan", "XiZang", "XinJiang", "YunNan",
			"ZheJiang", "BeiJing", "ShangHai", "TianJin", "ChongQing", "XiangGang", "AoMen", "AnQing", "BoZhou", "BangBu", "ChiZhou", "ChaoHu", "ChuZhou",
			"FuYang", "HeFei", "HuaiNan", "HuaiBei", "HuangShan", "LiuAn", "MaAnShan", "SuZhou", "TongLing", "WuHu", "XuanCheng", "FuZhou", "LongYan",
			"NanPing", "NingDe", "PuTian", "QuanZhou", "SanXing", "XiaMen", "ZhangZhou", "ChaoZhou", "DongGuan", "FoShan", "GuangZhou", "HeYuan", "HuiZhou",
			"JiangMen", "JieYang", "MeiZhou", "MaoMing", "QingYuan", "ShenZhen", "ShanTou", "ShaoGuan", "ShanWei", "YunFu", "YangJiang", "ZhuHai", "ZhongShan",
			"ZhaoQing", "ZhanXiang", "BeiHai", "BaiSe", "ChongZuo", "FangChengGang", "GuiLi", "GuiGang", "HeZhou", "HeChi", "LiuZhou", "LaiBin", "NanNing",
			"QinZhou", "WuZhou", "YuLin", "AnShun", "GuiYang", "LiuPanShui", "ZunYi", "BaiYin", "JinChang", "JiaYuGuan", "JiuQuan", "LanZhou", "QingYang",
			"TianShui", "WuWei", "ZhangYe", "HaiKou", "SanYa", "BaoDing", "ChengDe", "CangZhou", "HanDan", "HengShui", "LangFang", "QinHuangDao",
			"ShiJiaZhuang", "TangShan", "XingTai", "ZhangJiaKou", "AnYang", "JiaoZuo", "KaiFeng", "LuoYang", "HeBi", "LuoHe", "NanYang", "PingDingShan",
			"PuYang", "SanMenXia", "SHangQiu", "XinXiang", "XuChang", "XinYang", "ZhengZhou", "ZhouKou", "ZhuMaDian", "DaQing", "HaErBin", "HeGang", "HeiHe",
			"JiXi", "JiaMuSi", "MuDanJiang", "QiQiHaEr", "QiTaiHe", "SuiHua", "ShuangYaShan", "YiChun", "E\'Zhou", "HuangShi", "HuangGang", "JingZhou",
			"JingMen", "ShiYan", "WuHan", "XiangYang", "XiaoGan", "XianNing", "YiChang", "ChangSha", "ChangDe", "ChenZhou", "HengYang", "HuaiHua", "LouDi",
			"ShaoYang", "xiangTan", "YueYang", "YiYang", "YongZhou", "ZhuZhou", "ZhangJiaJie", "BaiShan", "BaiCheng", "ChangChun", "JiLin", "LiaoYuan",
			"SiPing", "SongYuan", "TongHua", "ChangZhou", "HuaiAn", "LianYunGang", "NanJing", "NanTong", "SuZhou", "SuQian", "TaiZhou", "WuXi", "XuZhou",
			"YanCheng", "YangZhou", "ZhenJiang", "FuZhou", "GanZhou", "JingDeZhen", "JiuJiang", "JiAn", "NanChang", "PingXiang", "ShangRao", "XinYu", "YingTan",
			"YiChun", "AnShan", "BenXi", "ChaoYang", "DaLian", "DanDong", "FuShun", "FuXin", "HuLuDao", "JinZhou", "LiaoYang", "PanJin", "ShenYang", "TieLing",
			"YingKou", "BaoTou", "ChiFeng", "E\'ErDuoSi", "HuHeHaoTe", "TongLiao", "WuHai", "GuYuan", "WuZhong", "YingChuan", "XiNing", "AnKang", "BaoJi",
			"HanZhong", "ShangLuo", "TongChuan", "WeiNan", "Xi\'An", "YaNan", "XianYang", "YuLin", "ChangZhi", "DaTong", "JinCheng", "LinFen", "ShuoZhou",
			"TaiYuan", "XinZhou", "YangQuan", "YunCheng", "BinZhou", "DongYing", "DeZhou", "HeZe", "JiNan", "JiNing", "LaiWu", "LinYi", "LiaoCheng", "QingDao",
			"RiZhao", "TaiAn", "WeiFang", "WeiHai", "YanTai", "ZiBo", "ZaoZhuang", "BaZhong", "ChengDu", "DeYang", "DaZhou", "GuangYuan", "GuangAn", "LuZhou",
			"LeShan", "MianYang", "MeiShan", "NeiJiang", "NanChong", "PanZhiHua", "SuiNing", "YaAn", "YiBin", "ZiGong", "Ziyang", "GaoXiong", "JiLong", "JiaYi",
			"TaiBei", "TaiZhong", "XinZhu", "LaSa", "KeLaMaYi", "WuLuMuQi", "BaoShan", "KunMing", "YuXi", "ZhaoTong", "HangZhou", "HuZhou", "JiaXing", "JinHua",
			"LiShui", "NingBo", "QuZhou", "ShaoXing", "TaiZhou", "WenZhou", "ZhouShan", "863", "973", "NSFC" });

	public static List<RegPair> countryRegPairList = new ArrayList<>();

	static {

		try {
			groupYearlist.add(new int[] { 1977, 1992 });
			groupYearlist.add(new int[] { 1992, 2000 });
			groupYearlist.add(new int[] { 2000, 2010 });
			groupYearlist.add(new int[] { 2010, 2017 });

			countryProps = loadAsProperties("country.properties");
			africaSet = loadAsSet("africa.txt");
			chineseFundList.addAll(loadAsSet("chinesefund.txt"));

			// Hong Kong
			// Hongkong
			// Aomen
			// Macao
			// Macau

			List<String> list = Arrays.asList(new String[] { "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
					"KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
					"SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY", "PR", "Gu", "Ap", "DC" });

			list = list.stream().map(str -> str + "( \\d{5})?\\.?$").collect(Collectors.toList());
			list.add(0, "USA");

			countryRegPairList.add(new RegPair("USA", (String[]) list.toArray(new String[list.size()])));
			countryRegPairList.add(new RegPair("England", new String[] { "England", "Scotland", "North Ireland", "U.K" }));
			countryRegPairList.add(new RegPair("Saint Kitts and Nevis", new String[] { "St Kitts & Nevi", "Saint Kitts and Nevis", "St. Kitts and Nevis" }));
			countryRegPairList.add(new RegPair("China", new String[] { "China" }));
			countryRegPairList.add(new RegPair("UAE", new String[] { "U Arab Emirates", "UAE", "United Arab Rep" }));

			countryRegPairList.add(new RegPair("Rep Of Congo", "Congo", new String[] { "Brazzaville", "Brazaville", "Mossaka", "Pointe, Noire", "Pointe Noire",
					"Kisangani", "LIKOUALA", "Owando", "Sibiti", "Loutété", "Dolisie", "Nkayi", "Impfondo", "Ouésso", "Madingou", "Benin" }));

			countryRegPairList.add(new RegPair("Rep Dem Congo", "Congo",
					new String[] { "Bunia", "Kalemie", "Ituri Forest", "Ngungu", "Mabali", "Maniema", "Uvira", "Congolese", "Lubuntu", "Kivu", "Nduye",
							"Nyankunde", "Epulu", "Kinshasa", "Kimpese", "Lwiro", "Bukavu", "Bwamanda", "Goma", "Lubumbashi", "Watsa", "Kinhasa", "Kinshasha",
							"Kikwit", "Nioki", "Mbujimayi", "Ituru", "Kananga", "Kisangani", "Kidu", "Matadi", "Mbandaka", "Likasi", "Mbuji Mayi", "Tshikapa",
							"Masina", "Kolwezi" }));

			countryRegPairList.add(new RegPair("Sudan", "Sudan", new String[] { "Khartoum" }));

			countryRegPairList.add(new RegPair("South Sudan", "Sudan",
					new String[] { "South Sudan", "Juba", "Winejok", "Malakal", "Wau", "Pajok", "Yei", "Yambio", "Aweil", "Gogrial", "Rumbek" }));

			countryRegPairList.add(new RegPair("South Africa", new String[] { "Limpopo Province", "Limpopo Provinc" }));

			countryRegPairList.add(new RegPair("Israel", new String[] { "Israel" }));

			countryRegPairList.add(new RegPair("ZIMBABWE", new String[] { "ZIMBABWE" }));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Properties loadAsProperties(String path) throws IOException {
		List<String> lineList = loadAsLines(path);
		Properties props = new Properties();

		lineList.parallelStream().forEach((line) -> {
			String[] splits = line.split("=");
			if (2 == splits.length) {
				props.setProperty(splits[0].trim(), splits[1].trim());
			}
		});

		return props;
	}

	private static Set<String> loadAsSet(String path) throws IOException {
		List<String> lineList = loadAsLines(path);
		return new HashSet<>(lineList);
	}

	private static List<String> loadAsLines(String path) throws IOException {
		String filePath = ClassLoader.getSystemResource(path).getPath();
		File file = new File(filePath);

		return FileUtils.readLines(file, Charset.forName("utf-8"));
	}

	public static String getYearGroup(String _year) {

		int year = Integer.parseInt(_year);

		Optional<int[]> findAny = groupYearlist.stream().filter((int[] years) -> {
			return years[0] <= year && year < years[1];
		}).findAny();

		if (findAny.isPresent()) {
			int[] years = findAny.get();

			return years[0] + "-" + years[1];
		}

		System.err.println("can not find year:" + _year);
		return "";
	}

	public static String convertCamelCase(String text) {
		Matcher matcher = Utils.casePattern.matcher(text);

		StringBuffer sb = new StringBuffer();
		int start = 0;

		while (matcher.find()) {

			String firstLetter = matcher.group(1);
			String otherLetter = matcher.group(2);

			sb.append(text.substring(start, matcher.start()));
			sb.append(firstLetter.toUpperCase());
			sb.append(otherLetter.toLowerCase());

			start = matcher.end();
		}

		sb.append(text.substring(start));
		return sb.toString();
	}

	static Pattern casePattern = Pattern.compile("(\\w{1})(\\w+)", Pattern.CASE_INSENSITIVE);

	public static boolean isChineseFundOrInst(String str) {
		return Utils.matchRegPair(str, Utils.chineseFundOrInstRegPair);
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

		return Utils.getChineseFundList().parallelStream().filter(fund -> {
			return normalizeFX.indexOf(fund) >= 0;
		}).findAny().isPresent();
	}

	public static String getMatchedKey(String text, List<RegPair> regPairList) {

		Optional<RegPair> findFirst = regPairList.stream().filter(regPair -> {
			return matchRegPair(text, regPair);
		}).findFirst();

		if (findFirst.isPresent()) {
			return findFirst.get().getName();
		}

		return "";
	}

	public static boolean matchRegPair(String text, RegPair regPair) {

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
	}

	public static String getCountryName(String name) {
		return countryProps.getProperty(name.replace(" ", "_"));
	}

	public static boolean isAfrica(String name) {
		return africaSet.contains(name);
	}

	public static List<String> getChineseFundList() {
		return chineseFundList;
	}

}
