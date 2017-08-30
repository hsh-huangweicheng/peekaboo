import { WosRecordUtils } from './wos_record_utils';
import { Result } from './../interfaces';
import * as _ from 'underscore';



export class MatrixCoperator {

    private coperatorData = {};

    private index = -1;

    public validCoperators: string[];

    constructor(private minCoperatorCount = 3) { }

    public addCoperators(coperators: string[]) {

    }

    public getValidCoperators() {
        if (!this.validCoperators) {
            let coperators = Object.keys(this.coperatorData);
            const json = {};
            Object.keys(this.coperatorData).forEach((self) => {
                Object.keys(this.coperatorData[self]).forEach((other) => {
                    if (this.coperatorData[self][other] >= this.minCoperatorCount) {
                        json[self] = true;
                        json[other] = true;
                    }
                });
            });
            this.validCoperators = Object.keys(json).sort();
        }
        return this.validCoperators;
    }

    public nextLine(): string {
        const coperators = this.getValidCoperators();
        const index = this.index;
        this.index++;

        if (-1 === index) {
            return ['_'].concat(coperators).join('\t');
        } else {
            const self = coperators[index];
            if (!self) {
                return null;
            }

            return [self].concat(coperators.map((other: string) => {
                try {
                    return this.coperatorData[self][other];
                } catch (e) {
                    console.error(self, other);
                    throw e;
                }
            })).join('\t');
        }
    }
}
