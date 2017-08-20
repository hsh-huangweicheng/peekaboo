import { Parser, WosRecord } from './../../interfaces';
import { WosParser } from './../wos_parser';
import { expect } from 'chai';
import 'mocha';

describe('wos_parser.ts', () => {


    let lines: string[];
    let parser: WosParser;

    before(() => {
        lines = wosContent.split(/\r?\n/);
        parser = new WosParser();
    })

    it('match', () => {
        const isMatch = parser.match(lines);
        expect(isMatch).be.true;
    });

    it('exact', () => {
        const list = parser.exact(lines);
        expect(list.length).eq(2);
        const record: WosRecord = list[0];
        expect(record.PT).equals('J');
        expect(record.AU).to.have.same.members(['Khelfi, MF', 'Kacimi, A']);
        expect(record.ID).equals('LINEARIZABLE SYSTEMS');
        expect(record.PA).equals('345 E 47TH ST, NEW YORK, NY 10017 USA');
        expect(record.DA).equals('2017-08-12');
    });

});

const wosContent = `FN Clarivate Analytics Web of Science
VR 1.0
PT J
AU Khelfi, MF
   Kacimi, A
AF Khelfi, Mohamed Faycal
   Kacimi, Abderrahmane
GP IEEE
TI Robust control with sliding mode for a quadrotor unmanned aerial vehicle
SO 2012 IEEE INTERNATIONAL SYMPOSIUM ON INDUSTRIAL ELECTRONICS (ISIE)
LA English
DT Proceedings Paper
CT 21st IEEE International Symposium on Industrial Electronics (ISIE)
CY MAY 28-31, 2012
CL Hangzhou, PEOPLES R CHINA
SP Inst Elect & Elect Engineers (IEEE), IEEE Ind Elect Soc (IES), Zhejiang Univ, Mitsubishi Elect & Elect, Fuji Elect, RS Components, Delta Elect, Silergy, Xiamen Kehua Hengsheng
DE feedback linearization; matching and unmatching uncertainties; Sliding
   control; Unmanned aerial vehicles
ID LINEARIZABLE SYSTEMS
AB A robust control approach denoted sliding control of MIMO nonlinear system based on the output feedback linearization is developed to attenuate the parametric uncertainties. The scheme is dedicated to model of unmanned aerial vehicles (the quadrotor UAV). We assume that the model of the plant is not precise, due to nondeterministic knowledge of inertias parameters. Tracking control is used to stabilize the equilibrium of the system, while all the state supposed to be measurable. The analysis is based on tracking errors during transients and at the steady state, on performance, stability and robustness with respect to plant uncertainties. Simulation results are carried out.
C1 [Khelfi, Mohamed Faycal] Univ Oran, RIIR Lab, Fac Sci, Oran, Algeria.
   [Khelfi, Mohamed Faycal; Kacimi, Abderrahmane] Univ Oran, Inst Maintain & Ind Safety, Oran, Algeria.
RP Khelfi, MF (reprint author), Univ Oran, RIIR Lab, Fac Sci, Oran, Algeria.; Khelfi, MF (reprint author), Univ Oran, Inst Maintain & Ind Safety, Oran, Algeria.
EM mf_khelfi@yahoo.fr; abderrahm2001@yahoo.fr
CR KANELLAKOPOULOS I, 1991, IEEE T AUTOMAT CONTR, V36, P1241, DOI 10.1109/9.100933
   Kanellakopoulus I., 1991, FDN ADAPTIVE CONTROL, P311
   Mokhtari A, 2006, ADV ROBOTICS, V20, P71, DOI 10.1163/156855306775275495
   SASTRY SS, 1989, IEEE T AUTOMAT CONTR, V34, P1123, DOI 10.1109/9.40741
   Slotine J.-J. E., 1991, APPL NONLINEAR CONTR
   Watanabe K, 2009, STUD COMPUT INTELL, V192, P83
   Xu HJ, 2001, P AMER CONTR CONF, P4351, DOI 10.1109/ACC.2001.945662
NR 7
TC 3
Z9 3
U1 0
U2 0
PU IEEE
PI NEW YORK
PA 345 E 47TH ST, NEW YORK, NY 10017 USA
BN 978-1-4673-0158-9
PY 2012
BP 886
EP 892
PG 7
WC Engineering, Electrical & Electronic
SC Engineering
GA BBZ14
UT WOS:000309118100149
OA No
DA 2017-08-12
ER

PT J
AU Cristofol, M
   Gaitan, P
   Ramoul, H
   Yamamoto, M
AF Cristofol, Michel
   Gaitan, Patricia
   Ramoul, Hichem
   Yamamoto, Masahiro
TI Identification of two coefficients with data of one component for a
   nonlinear parabolic system
SO APPLICABLE ANALYSIS
LA English
DT Article
DE parabolic systems; inverse problem; Carleman estimate; Lipschitz
   stability estimate
ID REACTION-DIFFUSION EQUATION; INVERSE PROBLEM; GLOBAL UNIQUENESS;
   CARLEMAN ESTIMATE; TERM
AB In this article, we consider a nonlinear parabolic system with two components and prove a stability estimate of Lipschitz type in determining two coefficients of the system by data of only one component. The main idea for the proof is a Carleman estimate.
C1 [Cristofol, Michel; Gaitan, Patricia] Lab Anal, F-13453 Marseille, France.
   [Ramoul, Hichem] Univ Badji Mokhtar, Dept Math, LMA, Annaba 23000, Algeria.
   [Yamamoto, Masahiro] Univ Tokyo, Dept Math, Meguro Ku, Tokyo 153, Japan.
RP Cristofol, M (reprint author), Lab Anal, 39 Rue F Joliot Curie, F-13453 Marseille, France.
EM cristo@latp.univ-mrs.fr
FU JSPS-CNRS
FX This work was supported by the JSPS-CNRS Bilateral Joint Project.
CR Benabdallah A, 2010, CR MATH, V348, P25, DOI 10.1016/j.crma.2009.11.001
   Benabdallah A, 2009, APPL ANAL, V88, P683, DOI 10.1080/00036810802555490
   Choulli M, 2006, COMMUN PUR APPL ANAL, V5, P447
   Cristofol M., COMP REND ACAD UNPUB
   Cristofol M., 2011, COMMU PURE IN PRESS
   Cristofol M, 2006, INVERSE PROBL, V22, P1561, DOI 10.1088/0266-5611/22/5/003
   DUCHATEAU P, 1985, J DIFFER EQUATIONS, V59, P155, DOI 10.1016/0022-0396(85)90152-4
   Egger H, 2005, INVERSE PROBL, V21, P271, DOI 10.1088/0266-5611/21/1/017
   Fernandez-Cara E, 2006, SIAM J CONTROL OPTIM, V45, P1395, DOI 10.1137/S0363012904439696
   Fursikov A. V, 2000, OPTIMAL CONTROL DIST
   Immanuvilov OYu, 1998, INVERSE PROBL, V14, P1229
   Immanuvilov O.Yu., 2003, RIMS KYOTO U, V39, P227
   Klibanov MV, 2004, INVERSE PROBL, V20, P1003, DOI 10.1088/0266-5611/20/4/002
   Ladyzenskaya O. A., 1968, LINEAR QUASILINEAR E
   LORENZI A, 1982, ANN MAT PUR APPL, V131, P145, DOI 10.1007/BF01765150
   Okuro A., 2001, DIFFUSION ECOLOGICAL
   Roques L, 2010, NONLINEARITY, V23, P675, DOI 10.1088/0951-7715/23/3/014
   Shigesada N., 1997, BIOL INVASIONS THEOR
   Smoller J., 1983, SHOCK WAVES REACTION
NR 19
TC 8
Z9 8
U1 0
U2 0
PU TAYLOR & FRANCIS LTD
PI ABINGDON
PA 4 PARK SQUARE, MILTON PARK, ABINGDON OX14 4RN, OXON, ENGLAND
SN 0003-6811
J9 APPL ANAL
JI Appl. Anal.
PY 2012
VL 91
IS 11
BP 2073
EP 2081
DI 10.1080/00036811.2011.583240
PG 9
WC Mathematics, Applied
SC Mathematics
GA 018YO
UT WOS:000309704200007
OA No
DA 2017-08-12
ER
`;