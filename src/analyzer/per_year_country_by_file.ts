import { FlattenCoperator } from './../utils/flatten_coperator';
import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as _ from 'underscore';

export class PerYearCountryByFileAnalyzer implements Analyzer {

    public name: string = '国家统计（目录）';

    private data = {};

    public scan(record: WosRecord, isDuplicate: boolean, filePath: string): void {
        const country = /([^\x00-\xff]+)/.exec(filePath)[1];
        const year = record[WosFields.出版年];
        this.data[year] = this.data[year] || {};
        this.data[year][country] = this.data[year][country] || 0;
        this.data[year][country]++;
    }

    public getResultList(): Result[] {
        return [{
            name: '',
            nextLine: (() => {

                let years: string[];

                return () => {

                    if (years) {
                        const year = years.shift();
                        if (year) {
                            return Object.keys(this.data[year]).map((country: string) => {
                                return [year, country, this.data[year][country]].join('\t');
                            }).join('\r\n');
                        }
                    } else {
                        years = Object.keys(this.data).sort();
                        return ['年', '国家', '次数'].join('\t');
                    }
                };
            })()
        }];
    }
}

