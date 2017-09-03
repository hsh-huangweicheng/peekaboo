import { Table } from './../utils/count_table';
import { FlattenCoperator } from './../utils/flatten_coperator';
import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as _ from 'underscore';

export class SubjectAnalyzer implements Analyzer {

    public name: string = '';

    private list: string[][] = [];

    private data = {};

    private index = -1;

    private table = new Table('学科（目录）', ['年', '国家', '学科', '篇次']);

    private coperateTable = new Table('学科（目录，合作）', ['年', '国家', '学科', '合作篇次']);

    private hasFoundingTable = new Table('学科（目录，有基金）', ['年', '国家', '学科', '合作篇次']);

    public scan(record: WosRecord, isDuplicate: boolean, filePath: string): void {
        const year = WosRecordUtils.getPublishYear(record);
        const country = WosRecordUtils.getCountryFromPath(filePath);
        const subjects = WosRecordUtils.getSubjects(record);
        const foundings = WosRecordUtils.getFoundings(record);
        const countries = WosRecordUtils.getCountries(record);
        // const outAfricaCountries = countries.filter(country => !WosRecordUtils.isAfricaCountry(country));

        subjects.forEach((subject: string) => {
            this.table.increaseCount(year, country, subject);

            if (countries.length > 1) {
                this.coperateTable.increaseCount(year, country, subject);
            }

            if (foundings.length) {
                this.hasFoundingTable.increaseCount(year, country, subject);
            }
        });

    }

    public getResultList(): Result[] {
        return [this.table.createResult(),
        this.coperateTable.createResult(),
        this.hasFoundingTable.createResult()];
    }
}

