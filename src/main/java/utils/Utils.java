package utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.sun.xml.internal.ws.api.ResourceLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import bean.RegPair;
import wos.WosRecord;

public class Utils {

    private static List<int[]> groupYearlist = new ArrayList<>();

    static Properties countryProps = new Properties();

    private static Set<String> africaSet = new HashSet<>();

    private static List<String> chineseFundList = new ArrayList<>();

    private static Map<String, String> subjectMap = new HashMap<>();

    static {
        subjectMap.put("Acoustics", "声学");
        subjectMap.put("Agricultural Economics & Policy", "农业经济与政策");
        subjectMap.put("Agricultural Engineering", "农业工程");
        subjectMap.put("Agriculture, Dairy & Animal Science", "农业，乳制品及动物科学");
        subjectMap.put("Agriculture, Multidisciplinary", "农业，多学科");
        subjectMap.put("Agronomy", "农学");
        subjectMap.put("Allergy", "过敏");
        subjectMap.put("Anatomy & Morphology", "解剖学与形态学");
        subjectMap.put("Andrology", "男科");
        subjectMap.put("Anesthesiology", "麻醉学");
        subjectMap.put("Anthropology", "人类学");
        subjectMap.put("Archaeology", "考古学");
        subjectMap.put("Architecture", "建筑学");
        subjectMap.put("Area Studies", "区域研究");
        subjectMap.put("Art", "艺术");
        subjectMap.put("Asian Studies", "亚洲研究");
        subjectMap.put("Astronomy & Astrophysics", "天文学与天体物理学");
        subjectMap.put("Audiology & Speech-Language Pathology", "听力学及言语语言病理学");
        subjectMap.put("Automation & Control Systems", "自动化及控制系统");
        subjectMap.put("Behavioral Sciences", "行为科学");
        subjectMap.put("Biochemical Research Methods", "生物化学研究方法");
        subjectMap.put("Biochemistry & Molecular Biology", "生物化学与分子生物学");
        subjectMap.put("Biodiversity Conservation", "生物多样性保护");
        subjectMap.put("Biology", "生物学");
        subjectMap.put("Biophysics", "生物物理学");
        subjectMap.put("Biotechnology & Applied Microbiology", "生物技术和应用微生物学");
        subjectMap.put("Business", "商业，金融");
        subjectMap.put("Business, Finance", "商业");
        subjectMap.put("Cardiac & Cardiovascular Systems", "心脏和心血管系统");
        subjectMap.put("Cell & Tissue Engineering", "细胞与组织工程");
        subjectMap.put("Cell Biology", "细胞生物学");
        subjectMap.put("Chemistry, Analytical", "分析化学");
        subjectMap.put("Chemistry, Applied", "应用化学");
        subjectMap.put("Chemistry, Inorganic & Nuclear", "无机化学与核化学");
        subjectMap.put("Chemistry, Medicinal", "医药化学");
        subjectMap.put("Chemistry, Multidisciplinary", "化学，多学科");
        subjectMap.put("Chemistry, Organic", "有机化学");
        subjectMap.put("Chemistry, Physical", "物理化学");
        subjectMap.put("Classics", "古典文学");
        subjectMap.put("Clinical Neurology", "神经病学");
        subjectMap.put("Communication", "传播学");
        subjectMap.put("Computer Science, Artificial Intelligence", "计算机科学，人工智能");
        subjectMap.put("Computer Science, Cybernetics", "计算机科学，控制论");
        subjectMap.put("Computer Science, Hardware & Architecture", "计算机科学，硬件");
        subjectMap.put("Computer Science, Information Systems", "计算机科学，信息系统");
        subjectMap.put("Computer Science, Interdisciplinary Applications", "计算机科学，跨学科应用");
        subjectMap.put("Computer Science, Software Engineering", "计算机科学，软件工程");
        subjectMap.put("Computer Science, Theory & Methods", "计算机科学理论与方法");
        subjectMap.put("Construction & Building Technology", "建筑与建筑技术");
        subjectMap.put("Criminology & Penology", "犯罪学及刑罚学");
        subjectMap.put("Critical Care Medicine", "重症医学");
        subjectMap.put("Crystallography", "结晶学");
        subjectMap.put("Cultural Studies", "文化研究");
        subjectMap.put("Dance", "舞蹈");
        subjectMap.put("Demography", "人口统计学");
        subjectMap.put("Dentistry, Oral Surgery & Medicine", "牙科，口腔外科，口腔医学");
        subjectMap.put("Dermatology", "皮肤科");
        subjectMap.put("Developmental Biology", "发育生理学");
        subjectMap.put("Ecology", "生态学");
        subjectMap.put("Economics", "经济学");
        subjectMap.put("Education & Educational Research", "教育与教育研究");
        subjectMap.put("Education, Scientific Disciplines", "教育科学");
        subjectMap.put("Education, Special", "特殊教育");
        subjectMap.put("Electrochemistry", "电化学");
        subjectMap.put("Emergency Medicine", "急诊医学");
        subjectMap.put("Endocrinology & Metabolism", "内分泌与代谢医学");
        subjectMap.put("Energy & Fuels", "能源与燃料");
        subjectMap.put("Engineering, Aerospace", "航空航天科学");
        subjectMap.put("Engineering, Biomedical", "生物医学工程");
        subjectMap.put("Engineering, Chemical", "化学工程");
        subjectMap.put("Engineering, Civil", "土木工程");
        subjectMap.put("Engineering, Electrical & Electronic", "电机与电子工程");
        subjectMap.put("Engineering, Environmental", "环境工程");
        subjectMap.put("Engineering, Geological", "地质工程");
        subjectMap.put("Engineering, Industrial", "工业工程");
        subjectMap.put("Engineering, Manufacturing", "制造工程");
        subjectMap.put("Engineering, Marine", "船舶工程");
        subjectMap.put("Engineering, Mechanical", "机械工程");
        subjectMap.put("Engineering, Multidisciplinary", "工程，多学科");
        subjectMap.put("Engineering, Ocean", "海洋工程");
        subjectMap.put("Engineering, Petroleum", "石油工程");
        subjectMap.put("Entomology", "昆虫学");
        subjectMap.put("Environmental Sciences", "环境科学");
        subjectMap.put("Environmental Studies", "环境研究");
        subjectMap.put("Ergonomics", "人类工程学");
        subjectMap.put("Ethics", "伦理学");
        subjectMap.put("Ethnic Studies", "民族研究");
        subjectMap.put("Evolutionary Biology", "进化生物学");
        subjectMap.put("Family Studies", "家庭研究");
        subjectMap.put("Film, Radio, Television", "电影，广播，电视");
        subjectMap.put("Fisheries", "渔业");
        subjectMap.put("Folklore", "民俗学");
        subjectMap.put("Food Science & Technology", "食品科学与技术");
        subjectMap.put("Forestry", "林学");
        subjectMap.put("Gastroenterology & Hepatology", "肠胃科学与肝病学");
        subjectMap.put("Genetics & Heredity", "遗传学与遗传");
        subjectMap.put("Geochemistry & Geophysics", "地球化学与地球物理");
        subjectMap.put("Geography", "地理学");
        subjectMap.put("Geography, Physical", "地理与物理");
        subjectMap.put("Geology", "地质学");
        subjectMap.put("Geosciences, Multidisciplinary", "地球科学，多学科");
        subjectMap.put("Geriatrics & Gerontology", "老年病学与老年学");
        subjectMap.put("Green & sustainable science & technology", "绿色可持续科学与技术");
        subjectMap.put("Gerontology", "老年学");
        subjectMap.put("Health Care Sciences & Services", "保健学与保健服务");
        subjectMap.put("Health Policy & Services", "卫生政策与服务");
        subjectMap.put("Hematology", "血液学");
        subjectMap.put("History ", "历史学");
        subjectMap.put("History & Philosophy of Science", "历史与哲学科学");
        subjectMap.put("History of Social Sciences", "社会科学历史");
        subjectMap.put("Horticulture", "园艺学");
        subjectMap.put("Hospitality, Leisure, Sport & Tourism", "酒店，休闲，体育与旅游");
        subjectMap.put("Humanities, Multidisciplinary", "与其它学科有关的人文科学");
        subjectMap.put("Imaging Science & Photographic Technology", "成像学与摄影工艺");
        subjectMap.put("Immunology", "免疫学");
        subjectMap.put("Industrial Relations & Labor", "劳动与劳资关系");
        subjectMap.put("Infectious Diseases", "传染病学");
        subjectMap.put("Information Science & Library Science", "信息学与图书馆学");
        subjectMap.put("Instruments & Instrumentation", "仪器及仪表学");
        subjectMap.put("Integrative & Complementary Medicine", "综合医学与补充医学");
        subjectMap.put("International Relations", "国际关系");
        subjectMap.put("Language & Linguistics", "语言与语言学");
        subjectMap.put("Law", "法律");
        subjectMap.put("Limnology", "湖沼(生物)学");
        subjectMap.put("Linguistics", "语言学");
        subjectMap.put("Literary Reviews", "文学评论");
        subjectMap.put("Literary Theory & Criticism", "文学理论与批评文学");
        subjectMap.put("Literature", "文学");
        subjectMap.put("Literature, African, Australian, Canadian", "非洲，澳大利亚，加拿大文学");
        subjectMap.put("Literature, American", "美国文学");
        subjectMap.put("Literature, British Isles", "不列颠群岛文学");
        subjectMap.put("Literature, German, Dutch, Scandinavian", "德国，荷兰，北欧文学");
        subjectMap.put("Literature, Romance", "罗曼语文学");
        subjectMap.put("Literature, Slavic", "斯拉夫语文学");
        subjectMap.put("Logic", "逻辑学");
        subjectMap.put("Management", "管理学");
        subjectMap.put("Marine & Freshwater Biology", "海洋及淡水生物学");
        subjectMap.put("Materials Science, Biomaterials", "生物材料科学");
        subjectMap.put("Materials Science, Ceramics", "陶瓷材料科学");
        subjectMap.put("Materials Science, Characterization & Testing", "材料科学，表征与试验");
        subjectMap.put("Materials Science, Coatings & Films", "材料科学，涂料与薄膜");
        subjectMap.put("Materials Science, Composites", "材料科学，复合材料");
        subjectMap.put("Materials Science, Multidisciplinary", "材料科学，多学科");
        subjectMap.put("Materials Science, Paper & Wood", "材料科学，纸张与木材");
        subjectMap.put("Materials Science, Textiles", "材料科学，纺织品");
        subjectMap.put("Mathematical & Computational Biology", "数学与计算生物学");
        subjectMap.put("Mathematics", "数学");
        subjectMap.put("Mathematics, Applied", "应用数学");
        subjectMap.put("Mathematics, Interdisciplinary Applications", "数学的跨学科应用");
        subjectMap.put("Mechanics", "力学");
        subjectMap.put("Medical Ethics", "医学伦理学");
        subjectMap.put("Medical Informatics", "医学信息学");
        subjectMap.put("Medical Laboratory Technology", "医学检验技术");
        subjectMap.put("Medicine, General & Internal", "内科医学");
        subjectMap.put("Medicine, Legal", "医药法律");
        subjectMap.put("Medicine, Research & Experimental", "医学研究和实验");
        subjectMap.put("Medieval & Renaissance Studies", "中世纪与文艺复兴研究");
        subjectMap.put("Metallurgy & Metallurgical Engineering", "冶金与冶金工程");
        subjectMap.put("Meteorology & Atmospheric Sciences", "气象学与大气科学");
        subjectMap.put("Microbiology", "微生物学");
        subjectMap.put("Microscopy", "显微镜学");
        subjectMap.put("Mineralogy", "矿物学");
        subjectMap.put("Mining & Mineral Processing", "采矿与矿物加工工程");
        subjectMap.put("Multidisciplinary Sciences", "多学科科学");
        subjectMap.put("Music", "音乐");
        subjectMap.put("Mycology", "真菌学");
        subjectMap.put("Nanoscience & Nanotechnology", "纳米科学与纳米技术");
        subjectMap.put("Neuroimaging", "神经影像学");
        subjectMap.put("Neurosciences", "神经科学");
        subjectMap.put("Nuclear Science & Technology", "原子核科学与技术");
        subjectMap.put("Nursing", "护理学");
        subjectMap.put("Nutrition & Dietetics", "营养学与膳食学");
        subjectMap.put("Obstetrics & Gynecology", "妇产科学");
        subjectMap.put("Oceanography", "海洋学");
        subjectMap.put("Oncology", "肿瘤学");
        subjectMap.put("Operations Research & Management Science", "运营研究与管理");
        subjectMap.put("Ophthalmology", "眼科学");
        subjectMap.put("Optics", "光学");
        subjectMap.put("Ornithology", "鸟类学");
        subjectMap.put("Orthopedics", "矫形技术");
        subjectMap.put("Otorhinolaryngology", "耳鼻喉科学");
        subjectMap.put("Paleontology", "古生物学");
        subjectMap.put("Parasitology", "寄生虫学");
        subjectMap.put("Pathology", "病理学");
        subjectMap.put("Pediatrics", "小儿科学");
        subjectMap.put("Peripheral Vascular Disease", "周围性血管疾病学");
        subjectMap.put("Pharmacology & Pharmacy", "药理学及药剂学");
        subjectMap.put("Philosophy", "哲学");
        subjectMap.put("Physics, Applied", "应用物理");
        subjectMap.put("Physics, Atomic, Molecular & Chemical", "物理，原子，分子，化学制品");
        subjectMap.put("Physics, Condensed Matter", "高分子物理");
        subjectMap.put("Physics, Fluids & Plasmas", "流体与等离子体物理");
        subjectMap.put("Physics, Mathematical", "数学物理学");
        subjectMap.put("Physics, Multidisciplinary", "与其它学科有关的物理学");
        subjectMap.put("Physics, Nuclear", "核物理");
        subjectMap.put("Physics, Particles & Fields", "粒子与场物理");
        subjectMap.put("Physiology", "生理学");
        subjectMap.put("Planning & Development", "规划与发展学");
        subjectMap.put("Plant Sciences", "植物学");
        subjectMap.put("Poetry", "诗歌");
        subjectMap.put("Political Science", "政治学");
        subjectMap.put("Polymer Science", "高分子科学");
        subjectMap.put("Primary Health Care", "初级卫生保健");
        subjectMap.put("Psychiatry", "精神病学");
        subjectMap.put("Psychology", "心理学");
        subjectMap.put("Psychology, Applied", "应用心理学");
        subjectMap.put("Psychology, Biological", "生物心理学");
        subjectMap.put("Psychology, Clinical", "临床心理学");
        subjectMap.put("Psychology, Developmental", "发展心理学");
        subjectMap.put("Psychology, Educational", "教育心理学");
        subjectMap.put("Psychology, Experimental", "实验心理学");
        subjectMap.put("Psychology, Mathematical", "数学/统计心理学");
        subjectMap.put("Psychology, Multidisciplinary", "与其它学科有关的心理学");
        subjectMap.put("Psychology, Psychoanalysis", "精神分析学");
        subjectMap.put("Psychology, Social", "社会心理学");
        subjectMap.put("Public Administration", "公共管理/行政");
        subjectMap.put("Public, Environmental & Occupational Health", "公共，环境，职业健康");
        subjectMap.put("Radiology, Nuclear Medicine & Medical Imaging", "放射学，核子医学与医学成像学");
        subjectMap.put("Rehabilitation", "康复学");
        subjectMap.put("Religion", "宗教");
        subjectMap.put("Remote Sensing", "遥感技术");
        subjectMap.put("Reproductive Biology", "生殖生物学");
        subjectMap.put("Respiratory System", "呼吸系统");
        subjectMap.put("Rheumatology", "风湿病学");
        subjectMap.put("Robotics", "机器人学");
        subjectMap.put("Social Issues", "社会问题");
        subjectMap.put("Social Sciences, Biomedical", "社会科学，生物医学");
        subjectMap.put("Social Sciences, Interdisciplinary", "社会科学,跨学科");
        subjectMap.put("Social Sciences, Mathematical Methods", "社会科学,数学方法");
        subjectMap.put("Social Work", "社会工作");
        subjectMap.put("Sociology", "社会学");
        subjectMap.put("Soil Science", "土壤科学");
        subjectMap.put("Spectroscopy", "光谱学");
        subjectMap.put("Sport Sciences", "运动科学");
        subjectMap.put("Statistics & Probability", "统计与概率");
        subjectMap.put("Substance Abuse", "药物滥用");
        subjectMap.put("Surgery", "外科");
        subjectMap.put("Telecommunications", "电信科学");
        subjectMap.put("Theater", "戏剧");
        subjectMap.put("Thermodynamics", "热力学");
        subjectMap.put("Toxicology", "毒理学");
        subjectMap.put("Transplantation", "移植学");
        subjectMap.put("Transportation", "交通运输学");
        subjectMap.put("Transportation Science & Technology", "交通科技");
        subjectMap.put("Tropical Medicine", "热带医学");
        subjectMap.put("Urban Studies", "城市发展研究");
        subjectMap.put("Urology & Nephrology", "泌尿学与肾脏学 ");
        subjectMap.put("Veterinary Sciences", "兽医学");
        subjectMap.put("Virology", "病毒学");
        subjectMap.put("Water Resources", "水资源");
        subjectMap.put("Women's Studies", "妇女研究");
        subjectMap.put("Zoology", "动物学");
        subjectMap.put("Arts & Humanities - Other Topics", "艺术人文及其他");
        subjectMap.put("History", "历史学");
        subjectMap.put("Government & Law", "政府与法律");
        subjectMap.put("Cardiovascular System & Cardiology", "心血管系统与心脏病学");
        subjectMap.put("Computer Science", "计算机科学");
        subjectMap.put("Agriculture", "农学");
        subjectMap.put("Legal Medicine", "法医学");
        subjectMap.put("Science & Technology - Other Topics", "科学技术及其他");
        subjectMap.put("Life Sciences & Biomedicine - Other Topics", "生命科学与生物医学及其他");
        subjectMap.put("Physics", "物理学");
        subjectMap.put("Social Sciences - Other Topics", "社会科学及其他");
        subjectMap.put("Business & Economics", "商业与经济学");
        subjectMap.put("Mathematical Methods In Social Sciences", "社会科学中的数学方法");
        subjectMap.put("Materials Science", "材料科学");
        subjectMap.put("Chemistry", "化学");
        subjectMap.put("Research & Experimental Medicine", "研究与实验医学");
        subjectMap.put("Engineering", "工程学");
        subjectMap.put("General & Internal Medicine", "普通内科");
        subjectMap.put("Physical Geography", "自然地理学");
        subjectMap.put("Environmental Sciences & Ecology", "环境科学与生态学");
        subjectMap.put("Neurosciences & Neurology", "神经科学和神经学");
        subjectMap.put("Film, Radio & Television", "电影、广播和电视");
        subjectMap.put("Biomedical Social Sciences", "生物医学社会科学");
        subjectMap.put("Biodiversity & Conservation", "生物多样性与保护");
    }

    public static RegPair chineseFundOrInstRegPair = new RegPair("China", new String[]{"PRC", "china", "chinese", "BeiJing", "ShangHai", "TianJin",
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
            "LiShui", "NingBo", "QuZhou", "ShaoXing", "TaiZhou", "WenZhou", "ZhouShan", "863", "973", "NSFC"});

    public static List<RegPair> countryRegPairList = new ArrayList<>();

    static {

        try {
            groupYearlist.add(new int[]{1977, 1992});
            groupYearlist.add(new int[]{1992, 2000});
            groupYearlist.add(new int[]{2000, 2010});
            groupYearlist.add(new int[]{2010, 2017});

            countryProps = loadAsProperties("country.properties");
            africaSet = loadAsSet("africa.txt");
            chineseFundList.addAll(loadAsSet("chinesefund.txt"));

            // Hong Kong
            // Hongkong
            // Aomen
            // Macao
            // Macau

            List<String> list = Arrays.asList(new String[]{"AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI", "ID", "IL", "IN", "IA", "KS",
                    "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI",
                    "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY", "PR", "Gu", "Ap", "DC"});

            list = list.stream().map(str -> str + "( \\d{5})?\\.?$").collect(Collectors.toList());
            list.add(0, "USA");

            countryRegPairList.add(new RegPair("USA", (String[]) list.toArray(new String[list.size()])));
            countryRegPairList.add(new RegPair("England", new String[]{"England", "Scotland", "North Ireland", "U.K"}));
            countryRegPairList.add(new RegPair("Saint Kitts and Nevis", new String[]{"St Kitts & Nevi", "Saint Kitts and Nevis", "St. Kitts and Nevis"}));
            countryRegPairList.add(new RegPair("China", new String[]{"China"}));
            countryRegPairList.add(new RegPair("UAE", new String[]{"U Arab Emirates", "UAE", "United Arab Rep"}));

            countryRegPairList.add(new RegPair("Rep Of Congo", "Congo", new String[]{"Brazzaville", "Brazaville", "Mossaka", "Pointe, Noire", "Pointe Noire",
                    "Kisangani", "LIKOUALA", "Owando", "Sibiti", "Loutété", "Dolisie", "Nkayi", "Impfondo", "Ouésso", "Madingou", "Benin"}));

            countryRegPairList.add(new RegPair("Rep Dem Congo", "Congo",
                    new String[]{"Bunia", "Kalemie", "Ituri Forest", "Ngungu", "Mabali", "Maniema", "Uvira", "Congolese", "Lubuntu", "Kivu", "Nduye",
                            "Nyankunde", "Epulu", "Kinshasa", "Kimpese", "Lwiro", "Bukavu", "Bwamanda", "Goma", "Lubumbashi", "Watsa", "Kinhasa", "Kinshasha",
                            "Kikwit", "Nioki", "Mbujimayi", "Ituru", "Kananga", "Kisangani", "Kidu", "Matadi", "Mbandaka", "Likasi", "Mbuji Mayi", "Tshikapa",
                            "Masina", "Kolwezi"}));

            countryRegPairList.add(new RegPair("Sudan", "Sudan", new String[]{"Khartoum"}));

            countryRegPairList.add(new RegPair("South Sudan", "Sudan",
                    new String[]{"South Sudan", "Juba", "Winejok", "Malakal", "Wau", "Pajok", "Yei", "Yambio", "Aweil", "Gogrial", "Rumbek"}));

            countryRegPairList.add(new RegPair("South Africa", new String[]{"Limpopo Province", "Limpopo Provinc"}));

            countryRegPairList.add(new RegPair("Israel", new String[]{"Israel"}));

            countryRegPairList.add(new RegPair("ZIMBABWE", new String[]{"ZIMBABWE"}));

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

    public static String getSubjectName(String key) {
        String name = subjectMap.get(key);
        if (StringUtils.isEmpty(name)) {
            return key;
        }
        return name;
    }

    public static boolean isChina(String country) {
        return "中国".equals(country);
    }
}
