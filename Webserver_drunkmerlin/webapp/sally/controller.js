var express = require('express');
var router = express.Router();
var path = require('path');
const crypto = require('crypto');
var cookieParser = require('cookie-parser');

var sql = require("mssql");

const { poolPromise } = require('../db.js')





// GET route for reading data
router.get('/', async (req, res) => {
  var nutzer_id = req.cookies.nutzer_id;

  if(nutzer_id === undefined){
    res.redirect('/login');
    return;
  }


  var gewicht = req.cookies.gewicht;
  var sex = req.cookies.sex;

  if((gewicht === undefined) || (sex === undefined)){
    res.redirect('/setting');
    return;
  }


  var items = req.cookies.items;

  if(items === undefined){
    var items_input = "";

  }else{

    var items_input = "";

    var array = JSON.parse(items); 
    //<li class="spinner-item" onclick="setItem('0.1(L) Wein (11%)')">0.1(L) Wein (11%)</li>

    for(let i=0; i<array.length; i++){
      items_input = items_input + "<li class=\"spinner-item\" onclick=\"setItem('" + array[i][1] + "(L) " + array[i][0] + " (" + array[i][2] + "%)')\">" + array[i][1] + "(L) " + array[i][0] + " (" + array[i][2] + "%)</li>";
    }

    console.log(items_input);

  }


  try {
    const pool = await poolPromise
    const result = await pool.request()
        .input('nutzer_id', sql.VarChar(50), nutzer_id)
        .query('select name, menge, prozent, zeitpunkt, id from drink where nutzer_id = @nutzer_id order by zeitpunkt desc;')      

    var anzahl = result.rowsAffected;

    if(anzahl < 1){
      //keine ergebnisse
      var html_string = "";
      var promille_gesamt = "0.0";
      var ausgabe_zeit = "In 0H und 0Min wieder nüchtern";

      res.render(__dirname + '/index.html', {input:html_string, promille:promille_gesamt, zeit:ausgabe_zeit, items:items_input});

    }else{
      var html_string = "test";
      html_string = "";

      for(var i=0; i < anzahl; i++){
        //console.log(result.recordset[i]);
        html_string += '<div class="drink" onclick="deleteDrink(' + result.recordset[i].id + ')">';
        html_string += '<div class="drink-name">' + result.recordset[i].name +'</div>';
        html_string += '<div class="drink-zeit">' + SQL_Datetime_zu_Ausgabe(result.recordset[i].zeitpunkt) + '</div>';
        html_string += '</div>';

        //console.log(html);
      }

      var promille_gesamt = 0;
      var anfangs_zeit;
      for(var j=0; j< anzahl; j++){
        promille_gesamt += getPromillefromProzentUndMenge(result.recordset[j].prozent, result.recordset[j].menge, sex, gewicht);

        if(j == (anzahl-1)){
          anfangs_zeit = result.recordset[j].zeitpunkt;
        }
      }

      promille_gesamt = Math.round (promille_gesamt * 1000) / 1000;

      var ausgabe_zeit = berechne_Zeit_aus_Promille(promille_gesamt, anfangs_zeit);

      res.render(__dirname + '/index.html', {input:html_string, promille:promille_gesamt, zeit:ausgabe_zeit, items:items_input});



    }


  } catch (err) {
    res.status(500)
    res.send(err.message)
  }


  function getPromillefromProzentUndMenge(prozent, menge, geschlecht, gewicht){
    var promille = 0;

    var gramm = alkohol_gramm(menge, prozent);
    gramm = gramm * 0.8; //es werden nur ca 80 % des alkohols aufgenommen

    if((geschlecht == 'm') || (geschlecht == 'M')){
        promille = gramm / (gewicht * 0.68);
    }else if((geschlecht == "w") || (geschlecht == "W")){
        promille = gramm / (gewicht * 0.55);
    }

    //console.log("getPromille: " + promille);
    return promille;

  }

  function alkohol_gramm(menge, prozent){
    var gramm = 0;

    gramm = menge * prozent * 8;

    //console.log("alkohol_gramm: " + gramm);
    return gramm;
  }

  function berechne_Zeit_aus_Promille(promille, anfangs_zeit){
    var d = anfangs_zeit;
    d.setHours(d.getHours() - 2);
    var anfangs_timestamp = Date.parse(d);


    var jetzt = Date.now();

    var differenz_in_Millis = jetzt - anfangs_timestamp;
    var differenz_in_Min = (differenz_in_Millis / 1000) / 60;


    var minuten = differenz_in_Min % 60;
    var stunden = (differenz_in_Min - minuten) / 60;

    minuten = Math.round(minuten);


    var ausgabe = "In " + stunden + "H und " + minuten + "Min wieder nüchtern";
    return ausgabe;
  }

  function SQL_Datetime_zu_Ausgabe(datetime){
    var d = datetime;
    d.setHours(d.getHours() - 2);

    var ausgabe = d.getDate() + "."+ d.getMonth() + "."  + d.getFullYear() + " " + d.getHours() + ":" + d.getMinutes() + " Uhr";
    return ausgabe;
  }


});


router.get('/delete', async (req, res) => {
  var nutzer_id = req.cookies.nutzer_id;
  var id = req.query.id; // $_GET["id"]


  try {
    const pool = await poolPromise
    const result = await pool.request()
        .input('nutzer_id', sql.VarChar(50), nutzer_id)
        .input('id', sql.VarChar(50), id)
        .query('delete from drink where nutzer_id = @nutzer_id and id=@id;')      

    res.redirect('/');
  } catch (err) {
    res.status(500)
    res.send(err.message)
  }
})



router.get('/add', async (req, res) => {
    var gewicht = req.cookies.gewicht;
    var sex = req.cookies.sex;

    var nutzer_id = req.cookies.nutzer_id;

    if((gewicht === undefined) || (sex === undefined)){
      res.redirect('/setting');
      return;
    }

    if(nutzer_id === undefined){
      res.redirect('/login');
      return;
    }



    var drink_base64 = req.query.drink;
    console.log(drink_base64);

    var buff = new Buffer(drink_base64, 'base64');
    var drink = buff.toString('ascii');

    if((drink == "Auswahl") || (drink == "Getränk hinzufügen") || (drink == "Getränk nachtragen")){
      //nichts tun
      res.redirect('/');
      return;
    }

    console.log("getLiter: " + getLiter(drink));
    console.log("getName: " + getName(drink));
    console.log("getProzent: " + getProzent(drink));



    try {
      const pool = await poolPromise
      const result = await pool.request()
          .input('nutzer_id', sql.VarChar(50), nutzer_id)
          .input('menge', sql.VarChar(50), getLiter(drink))
          .input('prozent', sql.VarChar(50), getProzent(drink))
          .input('name', sql.VarChar(50), getName(drink))
          .query('insert into drink (menge, prozent, name, nutzer_id) values (@menge, @prozent, @name, @nutzer_id);')      

      res.redirect('/');
    } catch (err) {
      res.status(500)
      res.send(err.message)
    }

    


});

router.get('/addItem', function(req, res, next){
  res.sendFile(path.join(__dirname + '/addItem.html'));

});

router.get('/nachtragen', function(req, res, next){
  var items = req.cookies.items;

  if(items === undefined){
    var items_input = "";

  }else{

    var items_input = "";

    var array = JSON.parse(items); 
    //<li class="spinner-item" onclick="setItem('0.1(L) Wein (11%)')">0.1(L) Wein (11%)</li>

    for(let i=0; i<array.length; i++){
      items_input = items_input + "<li class=\"spinner-item\" onclick=\"setItem('" + array[i][1] + "(L) " + array[i][0] + " (" + array[i][2] + "%)')\">" + array[i][1] + "(L) " + array[i][0] + " (" + array[i][2] + "%)</li>";
    }

    console.log(items_input);

  }

  res.render(__dirname + '/addendum.html', {items:items_input});
  //res.sendFile(path.join(__dirname + '/addendum.html'));
});


router.post('/addItem', function(req, res, next){
  var name = req.body.name;
  var menge = req.body.menge;
  var prozent = req.body.prozent.replace("Prozent Alkohol: ", "");
  var prozent = prozent.replace("%", "");

  //console.log("name: " + name + "  menge: " + menge + "  prozent:" + prozent + "test");

  var alt = req.cookies.items;

  if(alt === undefined){
    
    var alt = [
      [name, menge, prozent]
    ];

    var json = JSON.stringify(alt);

  }else{

    var array = JSON.parse(alt); 

    array.push([name, menge, prozent]);

    var json = JSON.stringify(array);

  }


  console.log(json);

  res.cookie('items', json);




  res.redirect('../');
});

router.get('/addendum', async (req, res) => {
    var gewicht = req.cookies.gewicht;
    var sex = req.cookies.sex;

    var nutzer_id = req.cookies.nutzer_id;

    if((gewicht === undefined) || (sex === undefined)){
      res.redirect('/setting');
      return;
    }

    if(nutzer_id === undefined){
      res.redirect('/login');
      return;
    }

    var drink_base64 = req.query.drink;
    var zeit_base64 = req.query.zeit;    

    var drink_buff = new Buffer(drink_base64, 'base64');
    var zeit_buff = new Buffer(zeit_base64, 'base64');

    var drink = drink_buff.toString('ascii');
    var zeit = zeit_buff.toString('ascii');

    console.log(zeit + "  " + drink);
    console.log(getName(drink));

    if((drink == "Auswahl") || (drink == "Getränk hinzufügen") || (drink == "Getränk nachtragen")){
      //nichts tun
      res.redirect('/');
      return;
    }

    var Jetzt = new Date();

    var monat = ['Januar','Februar','März','April','Mai','Juni','Juli','August','September',    
                    'Oktober','November','Dezember']

    var Zeit_als_String = Jetzt.getDay() + " " + monat[Jetzt.getMonth()] + " " + Jetzt.getFullYear() + " " + "00" + ":" + "00" + ":00 UTC" ;

    var timestamp = Date.parse('04 Dec 1995 00:12:00 GMT');
    console.log(timestamp);
    console.log(Zeit_als_String);

/*
    try {
      const pool = await poolPromise
      const result = await pool.request()
          .input('nutzer_id', sql.VarChar(50), nutzer_id)
          .input('menge', sql.VarChar(50), getLiter(drink))
          .input('prozent', sql.VarChar(50), getProzent(drink))
          .input('name', sql.VarChar(50), getName(drink))
          .query('insert into drink (menge, prozent, name, nutzer_id) values (@menge, @prozent, @name, @nutzer_id);')      

      res.redirect('/');
    } catch (err) {
      res.status(500)
      res.send(err.message)
    }

*/


  res.redirect('../');
});






function getLiter(text){
  text = text.substring(0,text.indexOf('('));
  text = text.replace(" ", "");
  return text;
}

function getName(text){
  text = text.substring(text.indexOf(')') + 1);
  text = text.substring(0, text.indexOf('('));
  text = text.replace(" ", "");
  return text;
}

function getProzent(text){
  text = text.substring(text.indexOf('(') + 1);
  text = text.substring(text.indexOf('(') + 1);
  text = text.substring(0, text.indexOf('%'));
  return text;
}










module.exports = router;
