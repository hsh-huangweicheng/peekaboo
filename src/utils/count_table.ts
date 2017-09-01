import { WosRecordUtils } from './wos_record_utils';
import { Result } from './../interfaces';
import * as _ from 'underscore';

export class Table {

    private data = {};

    constructor(private name: string, private headers: string[]) {

    }

    public increaseCount(...fields: string[]) {
        fields.reduce((obj, field, index: number) => {
            const isLast = (fields.length - 1) === index;
            if (isLast) {
                const normalFiledName = WosRecordUtils.getShortName(field, 6);
                if(!normalFiledName.trim()){
                    console.log(field);
                }
                obj[normalFiledName] = obj[normalFiledName] || 0;
                obj[normalFiledName]++;
            } else {
                obj[field] = obj[field] || {};
                return obj[field];
            }
        }, this.data);
    }

    public createResult(): Result {
        let firstFieldValues: string[];

        const getRecords = (obj, fieldValues?: any[], list?: any): string[][] => {
            const keys = Object.keys(obj);
            list = list || [];
            fieldValues = fieldValues || [];
            if (keys.length) {
                keys.forEach((key) => {
                    if ('number' === typeof obj[key]) {
                        list.push([...fieldValues, key, obj[key]]);
                    } else {
                        getRecords(obj[key], [...fieldValues, key], list);
                    }
                })
            }
            return list;
        }

        return (() => {
            return {
                name: this.name,
                nextLine: () => {
                    if (firstFieldValues) {
                        const firstFieldValue = firstFieldValues.shift();

                        if (firstFieldValue) {
                            return getRecords(this.data[firstFieldValue]).map((arr: string[]) => {
                                return [firstFieldValue, ...arr].map(v => WosRecordUtils.getChineseCountryName(v)).join('\t');
                            }).join('\r\n');
                        }

                    } else {
                        firstFieldValues = Object.keys(this.data);
                        return this.headers.join('\t');
                    }
                }
            }
        })();
    }
}