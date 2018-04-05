const _ = require("underscore");

const fs = require("fs");
const content =
  "" + fs.readFileSync("output.bak/3.3.3[合作]非洲外国家按年合作篇次.txt");
const lines = content.split(/\r\n/);

const headers = lines[0].split(/\t/).slice(1);
let years = [];
const groupBy = {};
const output = "";

lines.forEach(line => {
  const splits = line.split(/\t/);
  const firstField = splits[0];
  groupBy[firstField] = splits.slice(1).map((data, index) => {
    return {
      count: +data,
      country: headers[index]
    };
  });
  if (_.isNumber(+firstField)) {
    years.push(+firstField);
  }
});

const sum = list => {
  return list.reduce((total, count) => total + count);
};
years = _.compact(years);
years.sort();

let results = years.map(year => {
  const ret = {};
  const dataList = _.chain(groupBy[year])
    .filter(val => {
      return val.count > 5;
    })
    .sortBy(val => {
      return -val.count;
    })
    .value();

  const total = sum(_.pluck(dataList, "count"));

  let count = 0;
  let topList = [];

  const coreCountryCount = Math.sqrt(dataList[0].count) * 0.749;

  _.findIndex(dataList, val => {
    count = count + val.count;
    const percent = count / total;
    topList.push(val);
    if (percent > 0.8) {
      ret.year = year;
      ret.percent = percent;
      ret.topCountryCount = topList.length;
      ret.topCountryStr = _.pluck(topList, "country").join(",");
      ret.allCountryCount = dataList.length;
      ret.allCountryStr = _.pluck(dataList, "country").join(",");
      return true;
    }
  });

  const coreCountryDataList = _.chain(dataList)
    .filter(val => {
      return val.count >= coreCountryCount;
    })
    .value();

  ret.top10CountryCount = sum(_.pluck(dataList.slice(0, 10), "count")) / total;
  ret.top10CountryStr = _.pluck(dataList.slice(0, 10), "country").join(",");
  ret.coreCountryCount = coreCountryDataList.length;
  ret.coreCountryStr = _.pluck(coreCountryDataList, "country").join(",");

  return ret;
});

results = results.map(ret => {
  return [
    ret.year,
    ret.percent,
    ret.topCountryCount,
    ret.topCountryStr,
    ret.top10CountryCount,
    ret.top10CountryStr,
    ret.allCountryCount,
    ret.allCountryStr,
    ret.coreCountryCount,
    ret.coreCountryStr
  ].join("\t");
});

results.unshift(
  [
    "年",
    ">80%",
    ">80%个数",
    ">80%国家",
    "TOP10百分比",
    "TOP10国家",
    "所有国家数",
    "所有国家名",
    "核心国家数量",
    "核心国家"
  ].join("\t")
);
fs.writeFileSync("output.bak/国际合作集中度分析.txt", results.join("\n"));
console.log(`end,${results.length}`);
