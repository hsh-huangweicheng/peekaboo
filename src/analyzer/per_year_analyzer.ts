import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as fs from 'fs';

export class PerYearAnalyzer implements Analyzer {

    private yearFirstCountryCount: { [year: string]: { [country: string]: number } } = {};

    private yearAllCountryCount: { [year: string]: { [country: string]: number } } = {};

    public name: string = '各国每年论文数量';


    public scan(record: WosRecord, isDuplicate: boolean): void {
        if (isDuplicate) {
            return;
        }

        record.C1 = record.C1 || [];
        const year = record[WosFields.出版年];

        const countries = WosRecordUtils.getCountries(record)
            .filter(country => AfricaCountries[country.toLowerCase()]);

        // 只取第一作者的国家
        const firstCountry = countries[0];

        if (firstCountry) {
            this.yearFirstCountryCount[year] = this.yearFirstCountryCount[year] || {};
            this.yearFirstCountryCount[year][firstCountry] = this.yearFirstCountryCount[year][firstCountry] || 0;
            this.yearFirstCountryCount[year][firstCountry]++;
        }


        // 所有国家
        countries.forEach(country => {
            this.yearAllCountryCount[year] = this.yearAllCountryCount[year] || {};
            this.yearAllCountryCount[year][country] = this.yearAllCountryCount[year][country] || 0;
            this.yearAllCountryCount[year][country]++;
        });

    }

    public getResultList(): Result[] {


        const cb = (data) => {

            let years: string[];

            return () => {
                if (years) {
                    const year = years.shift();
                    if (data[year]) {
                        const countries = Object.keys(data[year]);
                        return countries.map((country: string) => {
                            return [year, AfricaCountries[country.toLowerCase()] || country, data[year][country]].join('\t');
                        }).join('\r\n');
                    }
                } else {
                    years = Object.keys(data);
                    return ['年份', '国家', '次数'].join('\t');
                }
            }
        }

        return [{
            name: '_第一作者',
            nextLine: cb(this.yearFirstCountryCount)
        }, {
            name: '_所有作者',
            nextLine: cb(this.yearAllCountryCount)
        }];

    }
}

