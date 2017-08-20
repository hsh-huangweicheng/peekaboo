import { Record, Parser, WosRecord } from './../interfaces';

const ArrayFields = ['AU', 'AF', 'BE', 'C1', 'CR'];

export class WosParser implements Parser {

    match(lines: string[]): boolean {
        return lines[0].trim() === 'FN Clarivate Analytics Web of Science';
    }

    exact(lines: string[]): WosRecord[] {

        let lastField: string;
        let field: string;
        let record: WosRecord;

        const list: WosRecord[] = [];

        while (lines.length) {

            const line: string = lines.shift();
            if (!line) {
                continue;
            }

            field = line.substr(0, 2).trim() || field;
            const contentOfLine = line.substr(3);

            const fieldChanged = record && record[lastField] && lastField && lastField !== field;
            if (fieldChanged) {
                const isArrayField = ArrayFields.indexOf(lastField) >= 0;
                if (!isArrayField) {
                    record[lastField] = record[lastField].join(' ');
                }
            }

            switch (field) {
                case 'PT':
                    record = {} as WosRecord;
                    list.push(record);
                    break;

                case 'ER':
                    record.__DB = 'wos';
                    record.__ID = record.UT;
                    record = null;
                    break;
            }

            if (record) {
                const isArrayField = ArrayFields.indexOf(field) >= 0;
                record[field] = record[field] || [];
                record[field].push(contentOfLine);
            }

            lastField = field;
        }

        return list;

    }

}