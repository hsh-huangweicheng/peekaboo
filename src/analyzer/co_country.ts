import { FlattenCoperator } from './../utils/flatten_coperator';
import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as _ from 'underscore';

export class CoCountryAnalyzer implements Analyzer {

    public name: string = '国家间合作';

    private list: string[][] = [];

    private coperator = new FlattenCoperator(['年', '国家','合作国家', '篇次']);

    public scan(record: WosRecord, isDuplicate: boolean): void {
        if (isDuplicate) {
            return;
        }

        const year = record[WosFields.出版年];

        let countries = WosRecordUtils.getCountries(record);

        if (countries.length) {
            this.coperator.addCoperators(year, countries.map((country) => {
                return { key: country, country: country, fields: [WosRecordUtils.getChineseCountryName(country) || country] };
            }));
        }
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

