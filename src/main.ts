import { AsyncUtils } from './utils/async_utils';
import { FileUtils } from './utils/file_utils';
import { ParserManager } from './parser/parser_manager';
import { Record } from './interfaces';
import { RecordManager } from './record_manager';
import { WosParser } from './parser/wos_parser';
import * as ProgressBar from 'progress';

export class Main {

    private recordManager: RecordManager = new RecordManager();
    private parserManager: ParserManager = new ParserManager();

    constructor() {
        this.parserManager.addParser(new WosParser());
    }

    public async start() {
        const filePaths = await FileUtils.getAllFilePaths('./wos数据');
        const bar = new ProgressBar(':current :filePath', { total: filePaths.length })
        let count = 0;
        await AsyncUtils.each(filePaths, async (filePath: string, index: number) => {

            const lines = await FileUtils.readLines(filePath);
            const parser = this.parserManager.findParser(lines);
            const recordList = parser.exact(lines);
            this.recordManager.addRecord(...recordList);

            console.log(`${count++}/${filePaths.length}`);
        });

        const recordList = this.recordManager.getRecordList('wos');
    }
}

new Main().start();