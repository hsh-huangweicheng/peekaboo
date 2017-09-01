import { Table } from './../utils/count_table';
import { FlattenCoperator } from './../utils/flatten_coperator';
import { WosRecordUtils, AfricaCountries } from './../utils/wos_record_utils';
import { Result, Analyzer, WosFields, WosRecord } from './../interfaces';
import * as _ from 'underscore';

export class OtherAnalyzer implements Analyzer {

    public name: string = '';

    /**
     * 没有合作的论文
     */
    private noCoperateTable = new Table('没有合作的论文', ['年', '国家', '篇数']);

    private hasCoperateTable = new Table('有合作的论文', ['年', '国家', '篇数']);

    /**
     * 没有基金的论文
     */
    private noFoundingTable = new Table('没有基金的论文', ['年', '国家', '篇数']);

    private hasFoundingTable = new Table('有基金的论文', ['年', '国家', '篇数']);

    public scan(record: WosRecord, isDuplicate: boolean, filePath: string): void {

        const year = WosRecordUtils.getPublishYear(record);
        const country = WosRecordUtils.getCountryFromPath(filePath);
        const foundings = WosRecordUtils.getFoundings(record);
        const countries = WosRecordUtils.getCountries(record);

        if (1 === countries.length) {
            this.noCoperateTable.increaseCount(year, country);
        } else {
            this.hasCoperateTable.increaseCount(year, country)

        }

        if (1 === foundings.length) {
            this.noFoundingTable.increaseCount(year, country);
        } else {
            this.hasFoundingTable.increaseCount(year, country);
        }
    }

    public getResultList(): Result[] {
        return [this.noCoperateTable.createResult(),
        this.hasCoperateTable.createResult(),
        this.noFoundingTable.createResult(),
        this.hasFoundingTable.createResult()];
    }
}

