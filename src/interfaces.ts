export interface Parser {
    onRecord: (record: Record) => void;
    exact(line: string): void;
}

export interface Result {
    name: string;
    nextLine: () => string;
}

export interface Record {
    __ID: string;
    __DB: string;
}

export interface Analyzer {
    name: string;
    scan(record: Record): void;
    getResultList(): Result[];
}

export class WosRecord implements Record {
    __ID: string;
    __DB: string;
    PT: string;
    AU: string[];
    AF: string[];
    BE: string[];
    GP: string;
    TI: string;
    SO: string;
    SE: string;
    LA: string;
    DT: string;
    CT: string;
    CY: string;
    CL: string;
    SP: string;
    HO: string;
    DE: string;
    ID: string;
    AB: string;
    C1: string[];
    RP: string;
    EM: string;
    CR: string[];
    NR: string;
    TC: string;
    Z9: string;
    U1: string;
    U2: string;
    PU: string;
    PI: string;
    PA: string;
    SN: string;
    BN: string;
    PY: string;
    BP: string;
    EP: string;
    PG: string;
    WC: string;
    FU: string;
    SC: string;
    GA: string;
    UT: string;
    OA: string;
    DA: string;
}

export const WosFields = {
    '文件名': 'FN',
    '版本号': 'VR',
    '出版物类型（J=期刊；B=书籍；S=丛书；P=专利）': 'PT',
    '作者': 'AU',
    '作者全名': 'AF',
    '书籍作者': 'BA',
    '团体作者': 'CA',
    '书籍团体作者': 'GP',
    '编者': 'BE',
    '文献标题': 'TI',
    '出版物名称': 'SO',
    '丛书标题': 'SE',
    '丛书副标题': 'BS',
    '语种': 'LA',
    '文献类型': 'DT',
    '会议标题': 'CT',
    '会议日期': 'CY',
    '会议地点': 'CL',
    '会议赞助方': 'SP',
    '会议主办方': 'HO',
    '作者关键词': 'DE',
    'Keywords Plus®': 'ID',
    '摘要': 'AB',
    '作者地址': 'C1',
    '通讯作者地址': 'RP',
    '电子邮件地址': 'EM',
    '基金资助机构和授权号': 'FU',
    '基金资助正文': 'FX',
    '引用的参考文献': 'CR',
    '引用的参考文献数': 'NR',
    'Web of Science 被引频次计数': 'TC',
    '被引频次总数（WoS、BCI 和 CSCD）': 'Z9',
    '出版商': 'PU',
    '出版商所在城市': 'PI',
    '出版商地址': 'PA',
    '国际标准期刊号 (ISSN)': 'SN',
    '国际标准书号 (ISBN)': 'BN',
    '长度为 29 个字符的来源文献名称缩写': 'J9',
    'ISO 来源文献名称缩写': 'JI',
    '出版日期': 'PD',
    '出版年': 'PY',
    '卷': 'VL',
    '期': 'IS',
    '特刊': 'SI',
    '子辑': 'PN',
    '增刊': 'SU',
    '开始页': 'BP',
    '结束页': 'EP',
    '文献编号': 'AR',
    '数字对象标识符 (DOI)': 'DI',
    '书籍的数字对象标识符 (DOI)': 'D2',
    '页数': 'PG',
    '章节数 (Book Citation Index)': 'P2',
    'Web of Science 类别': 'WC',
    '学科类别': 'SC',
    '文献传递号': 'GA',
    '入藏号': 'UT',
    '记录结束': 'ER',
    '文件结束': 'EF'
}