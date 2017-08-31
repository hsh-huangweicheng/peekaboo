const data = '2016 (14,504)2008 (5,482)2000 (2,871)1993 (2,037)2015 (13,027)2007 (5,057)1996 (2,621)1992 (1,890)2014 (11,777)2006 (4,267)1998 (2,595)1990 (1,851)2013 (10,417)2005 (3,601)1999 (2,592)1989 (1,844)2012 (9,316)2003 (3,597)1997 (2,477)1988 (1,718)2011 (8,209)2004 (3,541)1995 (2,414)1987 (1,674)2010 (7,138)2002 (3,220)1994 (2,383)1986 (1,576)2009 (6,653)2001 (2,997)1991 (2,063)1985 (1,465)';

const reg = /\(([,\d]+)\)/g;

let counts = [];
let matchs;
while (matchs = reg.exec(data)) {
    counts.push(matchs[1].replace(/[^\d]/, ''));
}

let years = [];
const reg2 = /\)?(\d+) \(/g;
while (matchs = reg2.exec(data)) {
    years.push(matchs[1]);
}

const results= years.map((year, index) => {
    return `${year}     ${counts[index]}`;
}).join('\r\n');
console.log(results);