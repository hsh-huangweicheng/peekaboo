export interface Parser {

    match(lines: string[]): boolean;

    exact(lines: string[]): Record[];

}

export interface Result {
    headers: string[];
    list: string[][];
}

export interface Record {
    __ID: string;
    __DB: string;
}

export interface Analyzer {

    scan(record): void;

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
    SC: string;
    GA: string;
    UT: string;
    OA: string;
    DA: string;
}