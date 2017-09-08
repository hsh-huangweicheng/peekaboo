import { Table } from './../utils/count_table';
import { FlattenCoperator } from './../utils/flatten_coperator';
import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as _ from 'underscore';

const list = ['DC', 'AR', 'PA', 'NY', 'CA', 'GA'];

export class CoCountryByDirectoryAnalyzer implements Analyzer {

    public name: string = '国家间合作（目录）';

    private list: string[][] = [];

    private data = {};

    private index = -1;

    private table = new Table('', ['年', '国家', '合作国家', '篇数']);

    public scan(record: WosRecord, isDuplicate: boolean, filePath: string): void {

        const year = WosRecordUtils.getPublishYear(record);
        let country = WosRecordUtils.getCountryFromPath(filePath);
        const coCountries = WosRecordUtils.getCountries(record);

        // 从RP字段中分析出具体的国家
        // if (_.contains(['刚果', '几内亚'], country)) {
        //     country = WosRecordUtils.getChineseCountryName(coCountries[0]) || country;
        // }


        coCountries.forEach((coCountry: string) => {
            if (list.indexOf(coCountry) >= 0) {
                console.log(`coCountry:${coCountry}`);
                console.log(record.C1.join('\r\n'));
            }
            if (coCountry)
                if (country !== coCountry) {
                    this.table.increaseCount(year, country, coCountry);
                }
        });
    }

    public getResultList(): Result[] {
        return [this.table.createResult()];
    }
}

