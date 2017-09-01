import { OtherAnalyzer } from './analyzer/other_analyzer';
import { SubjectAnalyzer } from './analyzer/subject_analyzer';
import { WosRecordUtils } from './utils/wos_record_utils';
import { CoCountryByDirectoryAnalyzer } from './analyzer/co_country_by_directory';
import { FoundingAnalyzer } from './analyzer/founding_analyzer';
import { PerYearCountryByFileAnalyzer } from './analyzer/per_year_country_by_file';
import { CoInstitutionAnalyzer } from './analyzer/co_institution';
import { CoCountryAnalyzer } from './analyzer/co_country';
import { PerYearAnalyzer } from './analyzer/per_year_analyzer';
import { AsyncUtils } from './utils/async_utils';
import { FileUtils } from './utils/file_utils';
import { Record, Analyzer, Result, WosRecord, Parser } from './interfaces';
import { WosParser } from './parser/wos_parser';
import * as fs from 'fs';
import * as readline from 'readline';

const unit = 8 * 1024 * 1024;
export class Main {

    private analyzerList: Analyzer[] = [];

    constructor() {
        this.analyzerList.push(new FoundingAnalyzer());
        this.analyzerList.push(new PerYearAnalyzer());
        this.analyzerList.push(new PerYearCountryByFileAnalyzer());
        this.analyzerList.push(new CoCountryAnalyzer());
        this.analyzerList.push(new CoInstitutionAnalyzer());
        this.analyzerList.push(new CoCountryByDirectoryAnalyzer());
        this.analyzerList.push(new SubjectAnalyzer());
        this.analyzerList.push(new OtherAnalyzer());
    }

    public async start() {
        const startTimestamp = Date.now();
        console.log(`start ${startTimestamp}`);
        const filePaths = await FileUtils.getAllFilePaths('./wos');
        const total = filePaths.length;
        const idMapping = {};

        let recordCount = 0;
        let duplicateCount = 0;
        const directoryDuplicateIDMap = {};
        const parser = new WosParser();
        parser.onRecord = (record: WosRecord, filePath: string) => {
            if (+record.PY < 1960) {
                return;
            }

            const ID = record.__ID;
            const countryFromPath = WosRecordUtils.getCountryFromPath(filePath);
            directoryDuplicateIDMap[countryFromPath] = directoryDuplicateIDMap[countryFromPath] || {};
            if (directoryDuplicateIDMap[countryFromPath][ID]) {
                directoryDuplicateIDMap[countryFromPath][ID]++;
                return;
            }
            directoryDuplicateIDMap[countryFromPath][ID] = 1;

            recordCount++;

            this.scan(record, filePath, !!idMapping[ID]);

            if (idMapping[ID]) {
                duplicateCount++;
            } else {
                idMapping[ID] = true;
            }
        };

        const cb = () => {
            const timestamp = Date.now();
            duplicateCount = 0;
            recordCount = 0;

            const filePath = filePaths.shift();
            if (filePath) {
                const rl = readline.createInterface({
                    input: fs.createReadStream(filePath)
                });

                rl.on('line', (line: string) => {
                    parser.exact(line, filePath);
                });

                rl.on('close', () => {
                    const memoryUsage = process.memoryUsage();
                    console.log(`${total - filePaths.length}/${total}\t重复:${duplicateCount}\t记录:${recordCount}\t时间:${(Date.now() - timestamp) / 1000}\t${filePath}`);
                    cb();
                });

            } else {
                this.printResult();
                console.log(`end ${(Date.now() - startTimestamp) / 1000} s`);
                const duplicateInDirectory = Object.keys(directoryDuplicateIDMap).map(key => {
                    return Object.keys(directoryDuplicateIDMap[key]).map((ID) => {
                        return [ID, directoryDuplicateIDMap[key][ID]];
                    }).filter(([ID, count]) => {
                        return count > 1;
                    }).map(arr => arr.join('\t')).join('\r\n');
                }).join('\r\n');
                console.log(`目录内重复：\r\n${duplicateInDirectory}`);
            }
        }

        cb();
    }

    private scan(record: WosRecord, filePath: string, isDuplicate: boolean) {
        record.C1 = record.C1 || [];
        this.analyzerList.forEach((analyzer: Analyzer) => {
            analyzer.scan(record, isDuplicate, filePath);
        });
    }

    private async printResult() {
        while (this.analyzerList.length) {
            const analyzer = this.analyzerList.shift();
            const results = analyzer.getResultList();
            while (results.length) {
                const result = results.shift();
                const outputPath = `./result/${analyzer.name}${result.name || ''}.txt`;
                let line: string;
                while ((line = result.nextLine()) !== undefined) {
                    if (line) {
                        fs.appendFileSync(outputPath, line + '\r\n', { encoding: 'utf-8' });
                    }
                }
                console.log(`print result to ${outputPath}`, new Date());
            }
        };
    }


    public async countLine() {
        const filePaths = await FileUtils.getAllFilePaths('./test');
        filePaths.forEach((path) => {
            const content = '' + fs.readFileSync(path)
            const reg = /^PT /gm;
            let count = 0;
            let matchs;
            while (reg.exec(content)) {
                count++;
            }
            if (500 !== count) {
                console.log(`${count}\t${path}`);
            }
        });
    }


}

new Main().start();