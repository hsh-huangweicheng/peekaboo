import { WosRecordUtils } from './../wos_record_utils';
import { Parser, WosRecord } from './../../interfaces';
import { expect } from 'chai';
import 'mocha';

describe('WosRecordUtils', () => {

    const countriesList = ['Saudi Arabia', 'Egypt', 'Egypt','USA'];
    const c1List = ['[Alnowibet, K.; Askar, S. S.] King Saud Univ, Stat & Operat Res Dept, Coll Sci, Riyadh, Saudi Arabia.',
        '[Askar, S. S.; Elsadany, A. A.] Mansoura Univ, Dept Math, Fac Sci, Mansoura 35516, Egypt.',
        '[Elsadany, A. A.] Suez Canal Univ, Fac Comp & Informat, Dept Basic Sci, Ismailia, Egypt.',
        '[Sulaiman, Rania S.; Shetty, Trupti; Grant, Maria B.; Corson, Timothy W.] Indiana Univ Sch Med, Dept Pharmacol & Toxicol, Indianapolis, IN 46202 USA.'];

    it('getCountries', () => {
        const countries = WosRecordUtils.getCountries({ C1: c1List } as any);
        expect(countries).members(countriesList);
    });

});