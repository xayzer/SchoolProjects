var express = require('express');
var router = express.Router();
var alasql = require('alasql');

alasql("CREATE TABLE IF NOT EXISTS test (user STRING, psw STRING) ");
alasql("INSERT INTO test VALUES ('LUCY', '3215142115213141')");
alasql("INSERT INTO test VALUES ('BEN', '321514dsa211513141')");

/* GET home page. */
router.get('/', function (req, res, next) {
  res.render('index', { title: 'Very Simple CTF' });
});

/* Handle the Form */
router.post('/form', function (req, res) {

  user = String(req.body.user)
  psw = String(req.body.psw)

  const results = alasql("SELECT * FROM test WHERE user='"+ user + "' AND psw='" + psw + "'");
  num = results.length

  if(num>=1){
    res.render('result',{judge:"Congratulations",word:"You find the flag CTF{PRETTY_EASY_ISN'T_IT}"})
  }else if(num==0){
    res.render('result',{judge:"Sorry! Wrong Answer!",word:"Still not enough hints?"})
  }
  
});

module.exports = router;
