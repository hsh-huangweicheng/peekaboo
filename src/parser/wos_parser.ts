import { Record, Parser, WosRecord } from './../interfaces';

const ArrayFields = ['AU', 'AF', 'BE', 'C1', 'CR'];

export class WosParser implements Parser {

    private lastField: string;
    private field: string;
    private record: WosRecord;

    onRecord: (record: WosRecord) => void;

    exact(line: string): void {

        const list: WosRecord[] = [];

        if (!line) {
            return;
        }

        this.field = line.substr(0, 2).trim() || this.field;
        const contentOfLine = line.substr(3);

        const fieldChanged = this.record && this.record[this.lastField] && this.lastField && this.lastField !== this.field;
        if (fieldChanged) {
            const isArrayField = ArrayFields.indexOf(this.lastField) >= 0;
            if (!isArrayField) {
                this.record[this.lastField] = this.record[this.lastField].join(' ');
            }
        }

        switch (this.field) {
            case 'PT':
                this.record = {} as WosRecord;
                list.push(this.record);
                break;

            case 'ER':

                if (this.record) {
                    this.record.__DB = 'wos';
                    this.record.__ID = this.record.UT;
                    this.onRecord(this.record);
                } else {
                    console.error(`can not find PT`);
                }

                this.record = null;
                break;
        }

        if (this.record) {
            const isArrayField = ArrayFields.indexOf(this.field) >= 0;
            this.record[this.field] = this.record[this.field] || [];
            this.record[this.field].push(contentOfLine);
        }

        this.lastField = this.field;

    }

}