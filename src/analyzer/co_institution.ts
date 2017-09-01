import { FlattenCoperator } from './../utils/flatten_coperator';
import { AfricaCountries, WosRecordUtils } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as _ from 'underscore';

export class CoInstitutionAnalyzer implements Analyzer {

    public name: string = '机构间合作';

    private list: string[][] = [];

    private coperator = new FlattenCoperator(['年', '国家A', '机构A', '国家B', '机构B', '篇次']);;

    constructor() {
    }

    public scan(record: WosRecord, isDuplicate: boolean): void {
        if (isDuplicate) {
            return;
        }

        let coperators = record.C1.map((line) => {
            const country = WosRecordUtils.getCountryByLine(line);
            const inst = WosRecordUtils.getInstitutionByLine(line);
            if (inst) {
                return {
                    country, key: [country, inst].join('_').toLowerCase(), fields: [country, inst]
                };
            } else {
                console.log([inst, inst, line].join('@@'));
            }
        });

        coperators = _.compact(coperators);
        const year = record[WosFields.出版年];

        this.coperator.addCoperators(year, coperators);
    }

    public getResultList(): Result[] {
        return [{
            name: '',
            nextLine: () => {
                return this.coperator.nextLine();
            }
        }];
    }
}

