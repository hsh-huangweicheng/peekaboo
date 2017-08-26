import { CoFoundingAnalyzer } from './analyzer/co_founding';
import { CoInstitutionAnalyzer } from './analyzer/co_institution';
import { CoCountryAnalyzer } from './analyzer/co_country';
import { InformationAnalyzer } from './analyzer/information';
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
        this.analyzerList.push(new CoFoundingAnalyzer());
        this.analyzerList.push(new PerYearAnalyzer());
        this.analyzerList.push(new CoCountryAnalyzer());
        // this.analyzerList.push(new InformationAnalyzer());
        this.analyzerList.push(new CoInstitutionAnalyzer());
    }

    public async start() {
        const startTimestamp = Date.now();
        console.log(`start ${startTimestamp}`);
        const filePaths = await FileUtils.getAllFilePaths('./test');
        const total = filePaths.length;
        const idMapping = {};

        let recordCount = 0;
        let duplicateCount = 0;

        const parser = new WosParser();
        parser.onRecord = (record: WosRecord) => {

            recordCount++;
            const ID = record.__ID;
            if (idMapping[ID]) {
                duplicateCount++;
            } else {
                idMapping[ID] = true;
                this.scan(record);
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
                    parser.exact(line);
                });

                rl.on('close', () => {
                    const memoryUsage = process.memoryUsage();
                    console.log(`${total - filePaths.length}/${total}\t重复:${duplicateCount}\t记录:${recordCount}\t时间:${(Date.now() - timestamp) / 1000}\t${filePath}`);
                    cb();
                });

            } else {
                this.printResult();
                console.log(`end ${(Date.now() - startTimestamp) / 1000} s`);
            }
        }

        cb();
    }

    private scan(record: WosRecord) {
        record.C1 = record.C1 || [];
        this.analyzerList.forEach((analyzer: Analyzer) => {
            analyzer.scan(record);
        });
    }

    private async printResult() {
        while (this.analyzerList.length) {
            const analyzer = this.analyzerList.shift();
            console.log(`process analyzer ${analyzer.name}`, new Date());
            const results = analyzer.getResultList();
            console.log(`       result count:${results.length}`, new Date());
            while (results.length) {
                const result = results.shift();
                const outputPath = `./result/${analyzer.name}${result.name || ''}.txt`;
                let line: string;
                while (line = result.nextLine()) {
                    fs.appendFileSync(outputPath, line + '\r\n', { encoding: 'utf-8' });
                }
                console.log(`print result to ${outputPath}`, new Date());
            }
        };
    }

}

new Main().start();