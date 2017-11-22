package consts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bean.RegPair;

public abstract class RegConsts {

	public static List<RegPair> countryRegPairList = new ArrayList<>();

	public static RegPair chineseFundOrInstRegPair = new RegPair("China", new String[] { "PRC", "china", "chinese", "BeiJing", "ShangHai", "TianJin", "ChongQing",
			"AnHui", "FuJian", "GuangDong", "GuangXi", "GuiZhou", "GanSu", "HaiNan", "HeBei", "HeNan", "HeiLongJiang", "HuBei", "HuNan", "JiLin", "JiangSu",
			"JiangXi", "LiaoNing", "NeiMengGu", "NingXia", "QingHai", "ShanXi", "ShanXi", "ShanDong", "SiChuan", "XiZang", "XinJiang", "YunNan", "ZheJiang",
			"BeiJing", "ShangHai", "TianJin", "ChongQing", "XiangGang", "AoMen", "AnQing", "BoZhou", "BangBu", "ChiZhou", "ChaoHu", "ChuZhou", "FuYang",
			"HeFei", "HuaiNan", "HuaiBei", "HuangShan", "LiuAn", "MaAnShan", "SuZhou", "TongLing", "WuHu", "XuanCheng", "FuZhou", "LongYan", "NanPing",
			"NingDe", "PuTian", "QuanZhou", "SanXing", "XiaMen", "ZhangZhou", "ChaoZhou", "DongGuan", "FoShan", "GuangZhou", "HeYuan", "HuiZhou", "JiangMen",
			"JieYang", "MeiZhou", "MaoMing", "QingYuan", "ShenZhen", "ShanTou", "ShaoGuan", "ShanWei", "YunFu", "YangJiang", "ZhuHai", "ZhongShan", "ZhaoQing",
			"ZhanXiang", "BeiHai", "BaiSe", "ChongZuo", "FangChengGang", "GuiLi", "GuiGang", "HeZhou", "HeChi", "LiuZhou", "LaiBin", "NanNing", "QinZhou",
			"WuZhou", "YuLin", "AnShun", "GuiYang", "LiuPanShui", "ZunYi", "BaiYin", "JinChang", "JiaYuGuan", "JiuQuan", "LanZhou", "QingYang", "TianShui",
			"WuWei", "ZhangYe", "HaiKou", "SanYa", "BaoDing", "ChengDe", "CangZhou", "HanDan", "HengShui", "LangFang", "QinHuangDao", "ShiJiaZhuang",
			"TangShan", "XingTai", "ZhangJiaKou", "AnYang", "JiaoZuo", "KaiFeng", "LuoYang", "HeBi", "LuoHe", "NanYang", "PingDingShan", "PuYang", "SanMenXia",
			"SHangQiu", "XinXiang", "XuChang", "XinYang", "ZhengZhou", "ZhouKou", "ZhuMaDian", "DaQing", "HaErBin", "HeGang", "HeiHe", "JiXi", "JiaMuSi",
			"MuDanJiang", "QiQiHaEr", "QiTaiHe", "SuiHua", "ShuangYaShan", "YiChun", "E\'Zhou", "HuangShi", "HuangGang", "JingZhou", "JingMen", "ShiYan",
			"WuHan", "XiangYang", "XiaoGan", "XianNing", "YiChang", "ChangSha", "ChangDe", "ChenZhou", "HengYang", "HuaiHua", "LouDi", "ShaoYang", "xiangTan",
			"YueYang", "YiYang", "YongZhou", "ZhuZhou", "ZhangJiaJie", "BaiShan", "BaiCheng", "ChangChun", "JiLin", "LiaoYuan", "SiPing", "SongYuan", "TongHua",
			"ChangZhou", "HuaiAn", "LianYunGang", "NanJing", "NanTong", "SuZhou", "SuQian", "TaiZhou", "WuXi", "XuZhou", "YanCheng", "YangZhou", "ZhenJiang",
			"FuZhou", "GanZhou", "JingDeZhen", "JiuJiang", "JiAn", "NanChang", "PingXiang", "ShangRao", "XinYu", "YingTan", "YiChun", "AnShan", "BenXi",
			"ChaoYang", "DaLian", "DanDong", "FuShun", "FuXin", "HuLuDao", "JinZhou", "LiaoYang", "PanJin", "ShenYang", "TieLing", "YingKou", "BaoTou",
			"ChiFeng", "E\'ErDuoSi", "HuHeHaoTe", "TongLiao", "WuHai", "GuYuan", "WuZhong", "YingChuan", "XiNing", "AnKang", "BaoJi", "HanZhong", "ShangLuo",
			"TongChuan", "WeiNan", "Xi\'An", "YaNan", "XianYang", "YuLin", "ChangZhi", "DaTong", "JinCheng", "LinFen", "ShuoZhou", "TaiYuan", "XinZhou",
			"YangQuan", "YunCheng", "BinZhou", "DongYing", "DeZhou", "HeZe", "JiNan", "JiNing", "LaiWu", "LinYi", "LiaoCheng", "QingDao", "RiZhao", "TaiAn",
			"WeiFang", "WeiHai", "YanTai", "ZiBo", "ZaoZhuang", "BaZhong", "ChengDu", "DeYang", "DaZhou", "GuangYuan", "GuangAn", "LuZhou", "LeShan",
			"MianYang", "MeiShan", "NeiJiang", "NanChong", "PanZhiHua", "SuiNing", "YaAn", "YiBin", "ZiGong", "Ziyang", "GaoXiong", "JiLong", "JiaYi", "TaiBei",
			"TaiZhong", "XinZhu", "LaSa", "KeLaMaYi", "WuLuMuQi", "BaoShan", "KunMing", "YuXi", "ZhaoTong", "HangZhou", "HuZhou", "JiaXing", "JinHua", "LiShui",
			"NingBo", "QuZhou", "ShaoXing", "TaiZhou", "WenZhou", "ZhouShan", "863", "973", "NSFC" });
	
	// Hong Kong
	// Hongkong
	// Aomen
	// Macao
	// Macau


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
		countryRegPairList.add(new RegPair("UAE", new String[] { "U Arab Emirates", "UAE", "United Arab Rep" }));

		countryRegPairList.add(new RegPair("Rep Of Congo", "Congo", new String[] { "Brazzaville", "Brazaville", "Mossaka", "Pointe, Noire", "Pointe Noire",
				"Kisangani", "LIKOUALA", "Owando", "Sibiti", "Loutété", "Dolisie", "Nkayi", "Impfondo", "Ouésso", "Madingou", "Benin" }));

		countryRegPairList.add(new RegPair("Rep Dem Congo", "Congo",
				new String[] { "Bunia", "Kalemie", "Ituri Forest", "Ngungu", "Mabali", "Maniema", "Uvira", "Congolese", "Lubuntu", "Kivu", "Nduye", "Nyankunde",
						"Epulu", "Kinshasa", "Kimpese", "Lwiro", "Bukavu", "Bwamanda", "Goma", "Lubumbashi", "Watsa", "Kinhasa", "Kinshasha", "Kikwit", "Nioki",
						"Mbujimayi", "Ituru", "Kananga", "Kisangani", "Kidu", "Matadi", "Mbandaka", "Likasi", "Mbuji Mayi", "Tshikapa", "Masina", "Kolwezi" }));

		countryRegPairList.add(new RegPair("Sudan", "Sudan", new String[] { "Khartoum" }));

		countryRegPairList.add(new RegPair("South Sudan", "Sudan",
				new String[] { "South Sudan", "Juba", "Winejok", "Malakal", "Wau", "Pajok", "Yei", "Yambio", "Aweil", "Gogrial", "Rumbek" }));

		countryRegPairList.add(new RegPair("South Africa", new String[] { "Limpopo Province","Limpopo Provinc" }));

		countryRegPairList.add(new RegPair("Israel", new String[] { "Israel" }));

		countryRegPairList.add(new RegPair("ZIMBABWE", new String[] { "ZIMBABWE" }));

	}

}
