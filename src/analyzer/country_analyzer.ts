import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import { Table } from './../utils/count_table';
import * as fs from 'fs';

export class CountryAnalyzer implements Analyzer {

    public name: string = '国家统计';

    private table = new Table('', ['年', '国家', '篇数']);
    private dirTable = new Table('(目录)', ['年', '国家', '篇数']);


    public scan(record: WosRecord, isDuplicate: boolean, filePath: string): void {
        const year = WosRecordUtils.getPublishYear(record);
        const country = WosRecordUtils.getCountryFromPath(filePath);

        this.dirTable.increaseCount(year, country);

        if (!isDuplicate) {
            const countries = WosRecordUtils.getCountries(record);
            countries.filter((country: string) => {
                return WosRecordUtils.isAfricaCountry(country);
            }).forEach((country: string) => {
                this.table.increaseCount(year, WosRecordUtils.getChineseCountryName(country));
            });
        }

    }

    public getResultList(): Result[] {
        return [this.table.createResult(), this.dirTable.createResult()];
    }
}

