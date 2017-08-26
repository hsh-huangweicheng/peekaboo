import { WosRecordUtils } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as fs from 'fs';

export class PerYearAnalyzer implements Analyzer {

    private yearCountMapping: { [year: string]: { [country: string]: number } } = {};

    public name: string = '各国每年论文数量';

    public countrySet: Set<string> = new Set();

    public scan(record: WosRecord): void {
        record.C1 = record.C1 || [];
        const year = record[WosFields.出版年];

        // 只取第一作者的国家
        const country = WosRecordUtils.getCountry(record.C1[0]) || '_';

        this.countrySet.add(country);

        this.yearCountMapping[year] = this.yearCountMapping[year] || {};
        this.yearCountMapping[year][country] = this.yearCountMapping[year][country] || 0;
        this.yearCountMapping[year][country]++;
    }

    public getResultList(): Result[] {
        const result = {} as Result;
        const years = Object.keys(this.yearCountMapping);
        years.sort();

        fs.writeFileSync('test.txt', JSON.stringify(this.yearCountMapping, null, 4));
        const countries = Array.from(this.countrySet);
        countries.sort();
        let index = -1;
        return [{
            name: '',
            nextLine: () => {
                const oldIndex = index++;
                if (-1 === oldIndex) {
                    return ['year', ...countries].join('\t');
                } else {
                    const year = years[oldIndex];
                    if (year) {
                        const countryMap = this.yearCountMapping[year];
                        return [year].concat(countries.map((country) => {
                            return '' + (countryMap[country] || 0);
                        })).join('\t');
                    }
                }
            }
        }];

    }
}

