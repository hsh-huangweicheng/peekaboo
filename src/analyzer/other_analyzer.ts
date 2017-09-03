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

    /**
     * 有合作的论文
     */
    private hasCoperateTable = new Table('有合作的论文', ['年', '国家', '篇数']);

    /**
     * 没有基金的论文
     */
    private noFoundingTable = new Table('没有基金的论文', ['年', '国家', '篇数']);

    /**
     * 有基金的论文
     */
    private hasFoundingTable = new Table('有基金的论文', ['年', '国家', '篇数']);


    /**
     * 仅非洲内部合作的论文
     */
    private onlyCoperateWithAfricaTable = new Table('仅非洲内合作的论文', ['年', '国家', '篇数']);

    /**
     * 仅与非洲外部合作的论文
     */
    private onlyCoperateWithOutAfricaTable = new Table('仅与非洲外合作的论文', ['年', '国家', '篇数']);

    public scan(record: WosRecord, isDuplicate: boolean, filePath: string): void {

        const year = WosRecordUtils.getPublishYear(record);
        const country = WosRecordUtils.getCountryFromPath(filePath);
        const foundings = WosRecordUtils.getFoundings(record);
        const countries = WosRecordUtils.getCountries(record);

        if (foundings.length) {
            this.hasFoundingTable.increaseCount(year, country);
        } else {
            this.noFoundingTable.increaseCount(year, country);
        }

        const africaCountries = countries.filter(country => WosRecordUtils.isAfricaCountry(country));

        // 未过滤仅有一个国家，说明就是本国家
        const noCoperate = 1 === countries.length;

        // 未过滤国家大于1个，说明有合作
        const hasCoperate = countries.length > 1;

        // 非洲国家，过滤后的非常国家大于1，且过滤前后个数一样，说明都是非洲国家
        const onlyCoperateWithinAfrica = africaCountries.length > 1 && africaCountries.length === countries.length;

        // 过滤前国家大于1，说明是有合作的，且过滤后非洲国家只有一个，也就是目录所指的非洲国家
        const onlyCoperateWithoutAfrica = countries.length > 1 && 1 === africaCountries.length;

        if (noCoperate) {
            this.noCoperateTable.increaseCount(year, country);
        }

        if (hasCoperate) {
            this.hasCoperateTable.increaseCount(year, country)
        }

        if (onlyCoperateWithinAfrica) {
            this.onlyCoperateWithAfricaTable.increaseCount(year, country);
        }

        if (onlyCoperateWithoutAfrica) {
            this.onlyCoperateWithOutAfricaTable.increaseCount(year, country);
        }
    }

    public getResultList(): Result[] {
        return [this.noCoperateTable.createResult(),
        this.hasCoperateTable.createResult(),
        this.onlyCoperateWithAfricaTable.createResult(),
        this.onlyCoperateWithOutAfricaTable.createResult(),
        this.noFoundingTable.createResult(),
        this.hasFoundingTable.createResult()];
    }
}

