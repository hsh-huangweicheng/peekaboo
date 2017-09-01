import { Table } from './../utils/count_table';
import { FlattenCoperator } from './../utils/flatten_coperator';
import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as _ from 'underscore';

export class SubjectAnalyzer implements Analyzer {

    public name: string = '学科（根据目录）';

    private list: string[][] = [];

    private data = {};

    private index = -1;

    private table = new Table('', ['年', '国家', '学科', '篇数']);

    public scan(record: WosRecord, isDuplicate: boolean, filePath: string): void {
        const year = WosRecordUtils.getPublishYear(record);
        const country = WosRecordUtils.getCountryFromPath(filePath);
        const subjects = WosRecordUtils.getSubjects(record);

        subjects.forEach((founding: string) => {
            this.table.increaseCount(year, country, founding);
        });
    }

    public getResultList(): Result[] {
        return [this.table.createResult()];
    }
}

