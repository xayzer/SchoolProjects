var express = require('express');
var router = express.Router();
/* Set Database */
var loki = require('lokijs');
var db = new loki('data.json', {
  autoload: true,
  autoloadCallback: databaseInitialize,
  autosave: true,
  autosaveInterval: 4000
});

// implement the autoloadback referenced in loki constructor
function databaseInitialize() {
  var surveys = db.getCollection("surveys");
  if (surveys === null) {
    surveys = db.addCollection("surveys");
  }
}

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Express' });
});

module.exports = router;

/*response to the survey submit*/
router.post('/surveys', function (req, res) {
  var surdb = db.getCollection("surveys");
  console.log(req.body);
  var result = surdb.insert(req.body);

  var cart = surdb.find({ "cartoon": req.body.cartoon });
  var chart1_data = countKeys(surdb.find(), "cartoon");
  var chart2_data = countKeys(cart, "character");
  var chart3_data = countAvgAges(cart, "character");

  res.status(201).json({ "chart1": chart1_data, "chart2": chart2_data, "chart3":chart3_data });
})

/*review the data from the db*/
router.get('/surveys', function (req, res) {
  var result = db.getCollection("surveys").find();
  res.render('surview', { surview: result });
})

function countKeys(dlist, key) {
  dic = {};
  dlist.forEach(element => {
    if (!dic.hasOwnProperty(element[key]))
      dic[element[key]] = 1;
    else
      dic[element[key]] += 1;
  });
  return dic;
}

function countAvgAges(dlist, key) {
  age_dic = {};
  num_dic = {};
  max_dic = {};
  dlist.forEach(element => {
    if (!age_dic.hasOwnProperty(element[key])) {
      age_dic[element[key]] = parseInt(element["age"]);
      num_dic[element[key]] = 1;
      max_dic[element[key]] = parseInt(element["age"]);
    }
    else {
      age_dic[element[key]] += parseInt(element["age"]);
      num_dic[element[key]] += 1;
      if (max_dic[element[key]]<parseInt(element["age"])){
        max_dic[element[key]] = parseInt(element["age"]);
      }
    }
  });
  Object.keys(age_dic).forEach(element => {
    age_dic[element] = parseInt(age_dic[element] / num_dic[element]);
  });
  return [age_dic,max_dic];
}