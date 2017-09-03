import { FileUtils } from './utils/file_utils';
import * as _ from 'underscore'
import * as fs from 'fs';

export class ListToMatrix {

    public convertToMatrix(list: string[][]): string[][] {

        const padCount = 10;
        const headers = list[0];
        const fileds = _.sortBy(list.slice(1), ([a, b]: string[], ) => {
            return `${a}-${b}`;
        });

        let xList = [];
        let yList = [];
        const mapping = {};
        fileds.forEach(([y, x, value]: string[]) => {
            xList.push(x);
            yList.push(y);
            mapping[x] = mapping[x] || {};
            mapping[x][y] = mapping[x][y] || value;
        });

        xList = _.uniq(xList);
        yList = _.uniq(yList);

        const retList = [['-', ...xList], ..._.chain(yList).map((y) => {
            return [y, ...xList.map((x) => {
                return mapping[x][y];
            })];
        }).value()];

        return retList;
    }

    toMatrix(from: string, out: string) {
        const content = '' + fs.readFileSync(from, { encoding: 'utf8' });
        let list: string[][] = content.split('\r\n').filter(str => str.trim()).map(str => str.split(/[\s,]+/));
        const outputCsv = this.convertToMatrix(list).map(s => s.join('\t')).join('\r\n');
        fs.writeFileSync(out, outputCsv, { encoding: 'utf8' });
        console.log(`转换三列表：${from} 到 ${out}`);
    }
}

const listToMatrix = new ListToMatrix();
FileUtils.getAllFilePaths('./三列表').then((filePaths) => {
    filePaths.forEach((filePath) => {
        if (/\.csv/.test(filePath)) {
            return;
        }
        const matchs = /(.*)\.(\w+)$/.exec(filePath);
        listToMatrix.toMatrix(filePath, `${matchs[0]}_矩阵.csv`);
    });
});


