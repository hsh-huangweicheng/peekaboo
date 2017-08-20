import { Parser } from './../interfaces';

export class ParserManager {

    private parserList: Parser[] = [];

    public addParser(parser: Parser) {
        this.parserList.push(parser);
    }

    public findParser(lines: string[]): Parser | undefined {
        return this.parserList.find((parser: Parser) => {
            return parser.match(lines);
        });
    }

}