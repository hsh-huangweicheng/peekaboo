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
    }

    public async start() {
        const startTimestamp = Date.now();
        console.log(`start ${startTimestamp}`);
        const filePaths = await FileUtils.getAllFilePaths('./wos');
        const total = filePaths.length;
        const idMapping = {};

        let recordCount = 0;
        let duplicateCount = 0;
        let directoryDuplicateCount = 0;

        const directoryDuplicateIDMap = {};
        const parser = new WosParser();
        parser.onRecord = (record: WosRecord, filePath: string) => {

            if (directoryDuplicateIDMap[record.ID]) {
                directoryDuplicateCount++;
                return;
            }

            directoryDuplicateIDMap[record.ID] = true;

            recordCount++;
            const ID = record.__ID;
            this.scan(record, filePath, !!idMapping[ID]);
            if (idMapping[ID]) {
                duplicateCount++;
            } else {
                idMapping[ID] = true;
            }
        };

        const cb = () => {
            const timestamp = Date.now();

            directoryDuplicateCount = 0;
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
                    console.log(`${total - filePaths.length}/${total}\t重复:${duplicateCount}\t记录:${recordCount}\t目录内重复:${directoryDuplicateCount}\t时间:${(Date.now() - timestamp) / 1000}\t${filePath}`);
                    cb();
                });

            } else {
                this.printResult();
                console.log(`end ${(Date.now() - startTimestamp) / 1000} s`);
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

}

new Main().start();