import { WosRecord, WosFields } from './../interfaces';
import * as _ from 'underscore';

const PathCountryReg = /([^\x00-\xff]+)/;

const CustomCountries = [/\b(USA)\b/i,
    /\b(China)\b/i,
    /\b(congo, dem rep)\b/i,
    /\b(Congo, Dem)\b/i,
    /\b(Egypt, Arab Rep)\b/i,
    /\b(sao tome & prin)\b/i,
    /\b(Cote d\'Ivoire)\b/i,
    /\b(guinea-bissau)\b/i,
    /\b(gambia, the)\b/i];

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
        return _.uniq(countries, v => v.toLowerCase());
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

        const matchs = CountryReg.exec(line);
        if (matchs) {
            let country = matchs[1].trim();

            const found = CustomCountries.find((reg) => {
                const suMatchs = reg.exec(country);
                if (suMatchs) {
                    country = suMatchs[1];
                    return true;
                }
                return false;
            });

            return country;
        }
    }

    public static getShortName(name: string, wordLimit: number = 4) {
        const shortName = name.split(/\W+/).slice(0, wordLimit).join(' ');
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
        return AfricaCountries[country.toLowerCase()];
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
