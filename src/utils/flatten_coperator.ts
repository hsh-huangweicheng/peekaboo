import { Coperator } from './../interfaces';
import { WosRecordUtils, AfricaCountries } from './wos_record_utils';

const splitChar = '::';

interface Tmp {
    field: string, count: number
}

export class FlattenCoperator {

    private data = {};

    private years: string[];

    constructor(private headers: string[], private minCount = 1) {

    }

    public addCoperators(year: string, coperators: Coperator[]) {
        for (let i = 0; i < coperators.length - 1; i++) {
            const self: Coperator = coperators[i];
            if (AfricaCountries[self.country]) {

                for (let j = i + 1; j < coperators.length; j++) {
                    const other = coperators[j];

                    // if (self.key !== other.key) {
                    this.data[year] = this.data[year] || {}
                    this.data[year][self.key] = this.data[year][self.key] || {};
                    this.data[year][self.key][other.key] = this.data[year][self.key][other.key] || {
                        field: [...self.fields, ...other.fields].join(':'),
                        count: 0
                    };
                    this.data[year][self.key][other.key].count++;
                    // }
                }
            }
        }
    }

    public nextLine(): string {

        if (this.years) {
            const year = this.years.shift();
            if (year) {
                const list = [];
                Object.keys(this.data[year]).forEach((selfKey) => {
                    Object.keys(this.data[year][selfKey]).forEach((otherKey) => {
                        const value: Tmp = this.data[year][selfKey][otherKey];
                        list.push([year, ...value.field.split(':').map(v => WosRecordUtils.getChineseCountryName(v)), value.count].join('\t'))
                    });
                });
                return list.join('\r\n');
            }
        } else {
            this.years = Object.keys(this.data);
            return this.headers.join('\t');
        }

    }
}