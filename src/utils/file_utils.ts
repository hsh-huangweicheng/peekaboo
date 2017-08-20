import * as readline from 'readline';
import * as fs from 'fs';
const walk = require('walk');

export class FileUtils {

    public static readLines(filePath: string): Promise<string[]> {

        return new Promise((resolve) => {
            const list: string[] = []
            const lineReader = readline.createInterface({
                input: fs.createReadStream(filePath)
            });

            lineReader.on('line', function (line) {
                list.push(line);
            });

            lineReader.on('close', () => {
                resolve(list);
            });
        });
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