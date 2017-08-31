import { I18N } from './wos_record_utils';
import { WosRecord, WosFields } from './../interfaces';
import * as _ from 'underscore';

const PathCountryReg = /([^\x00-\xff]+)/;

const CountryMap: { [key: string]: RegExp[] } = {
    'usa': [/\busa\b/, /\bal$/, /\bdc$/, /\bak$/, /\baz$/, /\bar$/, /\bca$/, /\bco$/, /\bct$/, /\bde$/, /\bfl$/, /\bga$/, /\bhi$/, /\bid$/, /\bil$/, /\bin$/, /\bia$/, /\bks$/, /\bky$/, /\bla$/, /\bme$/, /\bmd$/, /\bma$/, /\bmi$/, /\bmn$/, /\bms$/, /\bmo$/, /\bmt$/, /\bne$/, /\bnv$/, /\bnh$/, /\bnj$/, /\bnm$/, /\bny$/, /\bnc$/, /\bnd$/, /\boh$/, /\bok$/, /\bor$/, /\bpa$/, /\bri$/, /\bsc$/, /\bsd$/, /\btn$/, /\btx$/, /\but$/, /\bvt$/, /\bva$/, /\bwa$/, /\bwv$/, /\bwi$/, /\bwy$/],
    'china': [/\bchina\b/],
    'congo, dem': [/\bcongo, dem rep\b/, /\b\congo, dem\b/, /\bdem rep congo\b/],
    'congo, rep': [/\bcongo, rep\b/, /\brep of congo\b/, /\bcongo peopl rep\b/],
    'egypt': [/\begypt, arab rep\b/],
    'sao tome and principe': [/\bsao tome & prin\b/, /\bsao tome & principe\b/],
    'guinea bissau': [/\bguinea.?bissau\b/],
    'gambia': [/\bgambia\b/],
    'yemen': [/\byemen\b/],
    'uae': [/\bu arab emirates\b/],
    'u.k': [/\bu.k\b/, /\bengland\b/],
    'serbia': [/\bserbia\b/],
    'falkland': [/\bfalkland\b/],
    'dominica': [/\bdominica\b/],
    'saudi arabia': [/\bsaudi\b/],
};

const SearchCountries = Object.keys(CountryMap);

const CountryReg = /,?([^,\d]*).$/;
const FirstCommaPartreg = /([^,]*),?/;

export class WosRecordUtils {

    private static shortFiledNameToRealName = {};

    public static getFoundings(record: WosRecord): string[] {
        return (record.FU || '').split(';').map(str => {
            const matchs = /([^\(\[]*)/.exec(str);
            if (matchs) {
                return matchs[1].trim();
            }
            return str.trim();
        }).filter(str => str);
    }

    public static getInititutionWithCountry(record: WosRecord): string[] {
        return record.C1.map((line) => {
            return `${this.getCountryByLine(line)}_${this.getInstitutionByLine(line)}`;
        });
    }

    public static getInstitutions(record: WosRecord): string[] {
        const c1 = record.C1 || [];

        return c1.map((line: string) => {
            return this.getInstitutionByLine(line);
        }).filter(r => r);
    }

    public static getInstitution(record: WosRecord): string {
        return this.getInstitutionByLine(record.C1[0]);
    }

    public static getInstitutionByLine(line: string = ''): string {
        const index = line.indexOf(']');
        if (index >= 0) {
            line = line.substring(index + 1);
        }

        const matchs = /([^\,]*)/.exec(line);
        if (matchs) {
            return matchs[1].trim();
        }
    }

    public static getCountries(record: WosRecord): string[] {
        const countries = record.C1.map(line => this.getCountryByLine(line)).filter(v => v);
        return _.uniq(countries);
    }

    public static getPublishYear(record: WosRecord): string {
        return record[WosFields.出版年];
    }

    public static getCountryFromPath(filePath: string): string {
        return PathCountryReg.exec(filePath)[1];
    }

    public static getCountry(record: WosRecord): string {
        return this.getCountryByLine(record.C1[0]);
    }



    public static getCountryByLine(line: string): string {
        if (!line) {
            return;
        }

        line = line.toLowerCase();
        const matchs = CountryReg.exec(line);
        if (matchs) {
            let country = matchs[1].trim();


            const found = SearchCountries.find((key) => {
                return !!CountryMap[key].find((reg) => {
                    return reg.test(country);
                });
            });

            return found || country;
        }
    }

    public static getShortName(name: string, wordLimit: number = 4) {
        const splits = name.toLowerCase().split(/\W+/);
        const shortName = _.difference(splits, ['of', 'the', 'and']).slice(0, wordLimit).join(' ');
        // this.shortFiledNameToRealName[shortName] = name;
        return shortName;
    }

    public static getRealName(shortName: string) {
        return this.shortFiledNameToRealName[shortName] || shortName;
    }

    public static getFirstCommaPart(str: string) {
        return FirstCommaPartreg.exec(str)[1];
    }

    public static getChineseCountryName(country: string) {
        return I18N[country.toLowerCase()];
    }
}

export const AfricaCountries = {
    'Algeria': '阿尔及利亚',
    'Benin': '贝宁',
    'Botswana': '博茨瓦纳',
    'Burkina Faso': '布基纳法索',
    'Burundi': '布隆迪',
    'Cabo Verde': '佛得角',
    'Cameroon': '喀麦隆',
    'Central African Republic': '中非共和国',
    'Chad': '乍得',
    'Comoros': '科摩罗',
    'Congo': '刚果',
    'Congo, Dem Rep': '刚果（金）',
    'Congo, Rep': '刚果（布）',
    'Cote d\'Ivoire': '科特迪瓦',
    'Djibouti': '吉布提',
    'Egypt': '埃及',
    'Egypt, Arab Rep': '埃及',
    'Eritrea': '厄立特里亚国',
    'Ethiopia': '埃塞俄比亚',
    'Gabon': '加蓬',
    'Gambia': '冈比亚',
    'Ghana': '加纳',
    'Guinea Bissau': '几内亚比绍',
    'Guinea': '几内亚',
    'Guinea-Bissau': '几内亚比绍',
    'Kenya': '肯尼亚',
    'Lesotho': '莱索托',
    'Liberia': '利比里亚',
    'Libya': '利比亚',
    'Madagascar': '马达加斯加',
    'Malawi': '马拉维',
    'Mali': '马里',
    'Mauritania': '毛利塔尼亚',
    'Mauritius': '毛里求斯',
    'Morocco': '摩洛哥',
    'Mozambique': '莫桑比克',
    'Namibia': '纳米比亚',
    'Niger': '尼日尔',
    'Nigeria': '尼日利亚',
    'Rwanda': '卢旺达',
    'Sao Tome & Prin': '圣多美和普林西比',
    'Sao Tome and Principe': '圣多美和普林西比',
    'Senegal': '塞内加尔',
    'Seychelles': '塞舌尔共和国',
    'Sierra Leone': '塞拉里昂',
    'Somalia': '索马里',
    'South Africa': '南非',
    'South Sudan': '南苏丹',
    'Sudan': '苏丹',
    'Swaziland': '斯威士兰王国',
    'Tanzania': '坦桑尼亚联合共和国',
    'Togo': '多哥',
    'Tunisia': '突尼斯',
    'Uganda': '乌干达',
    'Zambia': '赞比亚',
    'Zimbabwe': '津巴布韦',
    'algeria': '阿尔及利亚',
    'angola': '安哥拉',
    'benin': '贝宁',
    'botswana': '博茨瓦纳',
    'burkina faso': '布基纳法索',
    'burundi': '布隆迪',
    'cabo verde': '佛得角',
    'cameroon': '喀麦隆',
    'central african republic': '中非共和国',
    'chad': '乍得',
    'comoros': '科摩罗',
    'congo': '刚果',
    'congo, dem rep': '刚果（金）',
    'congo, rep': '刚果（布）',
    'cote d\'ivoire': '科特迪瓦',
    'djibouti': '吉布提',
    'egypt': '埃及',
    'egypt, arab rep': '埃及',
    'eritrea': '厄立特里亚国',
    'ethiopia': '埃塞俄比亚',
    'gabon': '加蓬',
    'gambia': '冈比亚',
    'ghana': '加纳',
    'guinea bissau': '几内亚比绍',
    'guinea': '几内亚',
    'guinea-bissau': '几内亚比绍',
    'kenya': '肯尼亚',
    'lesotho': '莱索托',
    'liberia': '利比里亚',
    'libya': '利比亚',
    'madagascar': '马达加斯加',
    'malawi': '马拉维',
    'mali': '马里',
    'mauritania': '毛利塔尼亚',
    'mauritius': '毛里求斯',
    'morocco': '摩洛哥',
    'mozambique': '莫桑比克',
    'namibia': '纳米比亚',
    'niger': '尼日尔',
    'nigeria': '尼日利亚',
    'rwanda': '卢旺达',
    'sao tome & prin': '圣多美和普林西比',
    'sao tome and principe': '圣多美和普林西比',
    'senegal': '塞内加尔',
    'seychelles': '塞舌尔共和国',
    'sierra leone': '塞拉里昂',
    'somalia': '索马里',
    'south africa': '南非',
    'south sudan': '南苏丹',
    'sudan': '苏丹',
    'swaziland': '斯威士兰王国',
    'tanzania': '坦桑尼亚联合共和国',
    'togo': '多哥',
    'tunisia': '突尼斯',
    'uganda': '乌干达',
    'zambia': '赞比亚',
    'zimbabwe': '津巴布韦',
    'Angola': '安哥拉',
    'Congo, Dem. Rep.': '刚果（金）',
    'Congo, Rep.': '刚果（布）',
    'Egypt, Arab Rep.': '埃及，阿拉伯共和国',
    'Equatorial Guinea': '赤道几内亚',
    'Gambia, The': '冈比亚',
    'congo, dem. rep.': '刚果（金）',
    'congo, rep.': '刚果（布）',
    'egypt, arab rep.': '埃及，阿拉伯共和国',
    'equatorial guinea': '赤道几内亚',
    'gambia, the': '冈比亚'
};


const I18N = {
    'Congo, dem': '刚果(金)',
    'Dominica': '多米尼加',
    'Falkland': '福克兰',
    'Serbia': '塞尔维亚',
    'U.K': '英国',
    'UAE': '阿拉伯联合酋长国',
    'USA': '美国',
    'YEMEN': '也门',
    'Saudi Arabia': '沙特阿拉伯',
    'afars & issas': '阿法尔和伊萨',
    'afghanistan': '阿富汗',
    'africa': '非洲',
    'agadir': '阿加迪尔',
    'albania': '阿尔巴尼亚',
    'andorra': '安道尔',
    'anguilla': '安圭拉',
    'antigua & barbu': '安提瓜·巴尔布',
    'argentina': '阿根廷',
    'armenia': '亚美尼亚',
    'aryanah': '艾尔亚奈',
    'australia': '澳大利亚',
    'austria': '奥地利',
    'azerbaijan': '阿塞拜疆',
    'bahamas': '巴哈马',
    'bahrain': '巴林',
    'bangladesh': '孟加拉国',
    'barbados': '巴巴多斯',
    'belgium': '比利时',
    'belize': '伯利兹',
    'bermuda': '百慕大群岛',
    'bhutan': '不丹',
    'bolivia': '玻利维亚',
    'bophuthatswana': '博普塔茨瓦纳',
    'bosnia & herceg': '波斯尼亚和海尔',
    'brazil': '巴西',
    'brazil;': '巴西;',
    'brit virgin isl': '英国的处女岛',
    'britishvirgin': 'britishvirgin',
    'brunei': '文莱',
    'bulgaria': '保加利亚',
    'burma': '缅甸',
    'byelarus': 'byelarus',
    'cambodia': '柬埔寨',
    'canada': '加拿大',
    'cape verde': '佛得角',
    'cayman islands': '开曼群岛',
    'cent afr empire': '中部非洲帝国',
    'cent afr republ': '中部非洲共和国',
    'chile': '智利',
    'ciskei': '息斯基',
    'colombia': '哥伦比亚',
    'congo, rep': '刚果（布）',
    'cook islands': '库克群岛',
    'costa rica': '哥斯达黎加',
    'cote ivoire': '科特迪瓦科特迪瓦',
    'crimea': '克里米亚',
    'croatia': '克罗地亚',
    'cuba': '古巴',
    'cyprus': '塞浦路斯',
    'czech republic': '捷克共和国',
    'czechoslovakia': '捷克斯洛伐克',
    'denmark': '丹麦',
    'dominican rep': '多米尼加共和国',
    'ecuador': '厄瓜多尔',
    'el salvador': '萨尔瓦多',
    'equat guinea': '赤道几内亚',
    'estonia': '爱沙尼亚',
    'fed rep ger': '美联储代表德国',
    'fiji': '斐济',
    'finland': '芬兰',
    'fr polynesia': '从波利尼西亚',
    'france': '法国',
    'french guiana': '法属圭亚那',
    'ger dem rep': 'GER DEM代表',
    'germany': '德国',
    'gibraltar': '直布罗陀',
    'greece': '希腊',
    'greenland': '格陵兰岛',
    'grenada': '格林纳达',
    'guadeloupe': '瓜德罗普岛',
    'guatemala': '瓜地马拉',
    'guernsey': '根西岛',
    'guyana': '圭亚那',
    'haiti': '海地',
    'honduras': '洪都拉斯',
    'hong kong': '香港',
    'hungary': '匈牙利',
    'iceland': '冰岛',
    'india': '印度',
    'indonesia': '印度尼西亚',
    'iran': '伊朗',
    'iraq': '伊拉克',
    'iraw': '腾丰',
    'ireland': '爱尔兰',
    'israel': '以色列',
    'italy': '意大利',
    'ivory coast': '象牙海岸',
    'jamaica': '牙买加',
    'japan': '日本',
    'jersey': '泽西',
    'jordan': '乔丹',
    'kazakhstan': '哈萨克斯坦',
    'kiribati': '基里巴斯',
    'kosovo': '科索沃',
    'kuwait': '科威特',
    'kyrgyzstan': '吉尔吉斯斯坦',
    'laos': '老挝',
    'latvia': '拉脱维亚',
    'lebanon': '黎巴嫩',
    'liechtenstein': '列支敦士登',
    'lithuania': '立陶宛',
    'luxembourg': '卢森堡',
    'macedonia': '马其顿',
    'malagasy republ': '马达加斯加共和国',
    'malaysia': '马来西亚',
    'maldives': '马尔代夫',
    'malta': '马耳他',
    'marshall island': '马绍尔群岛',
    'martinique': '马提尼克',
    'mexico': '墨西哥',
    'micronesia': '密克罗尼西亚',
    'middele e': '中E',
    'moldova': '摩尔多瓦',
    'monaco': '摩纳哥',
    'mongol peo rep': '蒙古人民代表',
    'montenegro': '黑山',
    'muretania': 'muretania',
    'myanmar': '缅甸',
    'nepal': '尼泊尔',
    'neth antilles': '荷属安第列斯',
    'netherlands': '荷兰',
    'new caledonia': '新喀里多尼亚',
    'new zealand': '新西兰',
    'nicaragua': '尼加拉瓜',
    'north ireland': '北爱尔兰',
    'north korea': '朝鲜',
    'norway': '挪威',
    'oman': '阿曼',
    'pakistan': '巴基斯坦',
    'palau': '帕劳',
    'palestine': '巴勒斯坦',
    'palestinian ter': '巴勒斯坦之三',
    'panama': '巴拿马',
    'papua n guinea': '巴布亚新几内亚',
    'paraguay': '巴拉圭',
    'peoples r china': 'R中国人民',
    'peru': '秘鲁',
    'philippines': '菲律宾',
    'poland': '波兰',
    'portugal': '葡萄牙',
    'puerto rico': '波多黎各',
    'qatar': '卡塔尔',
    'rabat maroc': '拉巴特摩洛哥',
    'rep congo': '刚果共和国',
    'rep of georgia': '代表乔治亚',
    'reunion': '重聚',
    'romania': '罗马尼亚',
    'russia': '俄罗斯',
    'samoa': '萨摩亚',
    'san marino': '圣马力诺',
    'saudi arabia': '沙特阿拉伯',
    'scotland': '苏格兰',
    'senegambia': '塞内冈比亚',
    'sharja': '孤儿沙加',
    'singapore': '新加坡',
    'slovakia': '斯洛伐克',
    'slovenia': '斯洛文尼亚',
    'solomon islands': '所罗门群岛',
    'south': '南方',
    'south korea': '韩国',
    'spain': '西班牙',
    'sri lanka': '斯里兰卡',
    'st kitts & nevi': '圣基茨岛和痣',
    'st lucia': '圣露西亚',
    'st vincent': '圣文森',
    'surinam': '苏里南',
    'sweden': '瑞典',
    'switzerland': '瑞士',
    'syria': '叙利亚',
    'taiwan': '台湾',
    'tajikistan': '塔吉克斯坦',
    'tajikstan': '塔吉克斯坦',
    'thailand': '泰国',
    'timor leste': '东帝汶',
    'tonga': '汤加',
    'transkei': '特兰斯凯',
    'trinid & tobago': 'trinid &多巴哥',
    'troyes': '特鲁瓦',
    'turkey': '火鸡',
    'turkmenistan': '土库曼斯坦',
    'tuvalu': '图瓦卢',
    'uae': '阿联酋',
    'ukraine': '乌克兰',
    'united kingdom': '联合王国',
    'upper volta': '沃尔塔',
    'uruguay': '乌拉圭',
    'ussr': '苏联',
    'utara malaysia': '北马来西亚',
    'uzbekistan': '乌兹别克斯坦',
    'vanuatu': '瓦努阿图',
    'vatican': '梵蒂冈',
    'venda': '文达',
    'venezuela': '委内瑞拉',
    'vietnam': '越南',
    'w ind assoc st': 'W ind公司ST',
    'wales': '威尔士',
    'western sahara': '西撒哈拉',
    'western samoa': '萨摩亚西部',
    'yugoslavia': '南斯拉夫',
    'zaire': '扎伊尔',
    'algeria': '阿尔及利亚',
    'angola': '安哥拉',
    'benin': '贝宁',
    'botswana': '博茨瓦纳',
    'burkina faso': '布基纳法索',
    'burundi': '布隆迪',
    'cabo verde': '佛得角',
    'cameroon': '喀麦隆',
    'central african republic': '中非共和国',
    'chad': '乍得',
    'comoros': '科摩罗',
    'congo': '刚果',
    'congo, dem rep': '刚果（金）',
    'cote d\'ivoire': '科特迪瓦',
    'djibouti': '吉布提',
    'egypt': '埃及',
    'egypt, arab rep': '埃及',
    'eritrea': '厄立特里亚国',
    'ethiopia': '埃塞俄比亚',
    'gabon': '加蓬',
    'gambia': '冈比亚',
    'ghana': '加纳',
    'guinea bissau': '几内亚比绍',
    'guinea': '几内亚',
    'guinea-bissau': '几内亚比绍',
    'kenya': '肯尼亚',
    'lesotho': '莱索托',
    'liberia': '利比里亚',
    'libya': '利比亚',
    'madagascar': '马达加斯加',
    'malawi': '马拉维',
    'mali': '马里',
    'mauritania': '毛利塔尼亚',
    'mauritius': '毛里求斯',
    'morocco': '摩洛哥',
    'mozambique': '莫桑比克',
    'namibia': '纳米比亚',
    'niger': '尼日尔',
    'nigeria': '尼日利亚',
    'rwanda': '卢旺达',
    'sao tome & prin': '圣多美和普林西比',
    'sao tome and principe': '圣多美和普林西比',
    'senegal': '塞内加尔',
    'seychelles': '塞舌尔共和国',
    'sierra leone': '塞拉里昂',
    'somalia': '索马里',
    'south africa': '南非',
    'south sudan': '南苏丹',
    'sudan': '苏丹',
    'swaziland': '斯威士兰王国',
    'tanzania': '坦桑尼亚联合共和国',
    'togo': '多哥',
    'tunisia': '突尼斯',
    'uganda': '乌干达',
    'zambia': '赞比亚',
    'zimbabwe': '津巴布韦',
    'Angola': '安哥拉',
    'Congo, Dem. Rep.': '刚果（金）',
    'Congo, Rep.': '刚果（布）',
    'Egypt, Arab Rep.': '埃及，阿拉伯共和国',
    'Equatorial Guinea': '赤道几内亚',
    'Gambia, The': '冈比亚',
    'congo, dem. rep.': '刚果（金）',
    'congo, rep.': '刚果（布）',
    'egypt, arab rep.': '埃及，阿拉伯共和国',
    'equatorial guinea': '赤道几内亚',
    'gambia, the': '冈比亚'
}