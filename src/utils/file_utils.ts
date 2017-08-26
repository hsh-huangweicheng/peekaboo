import * as readline from 'readline';
import * as fs from 'fs';
const walk = require('walk');

export class FileUtils {

    public static readLines(filePath: string, onLine: (line: string) => void, onClose: Function): void {
        var rl = readline.createInterface({
            input: fs.createReadStream(filePath)
        });

        rl.on('line', onLine);
        rl.on('close', onLine);
    }

    public static getAllFilePaths(dir: string): Promise<string[]> {

        const list: string[] = [];
        return new Promise((resolve) => {
            const walker = walk.walk(dir);

            walker.on('file', function (root: string, fileStats, next) {
                list.push(`${root}/${fileStats.name}`);
                next();
            });

            walker.on('end', function () {
                resolve(list);
            });
        });

    }

}