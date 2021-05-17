var express = require('express');
var router = express.Router();
var path = require('path');
const crypto = require('crypto');
var cookieParser = require('cookie-parser');

var sql = require("mssql");

const { poolPromise } = require('../db.js')



// GET route for reading data
router.get('/', function (req, res, next) {
  res.sendFile(path.join(__dirname + '/register.html'));
});

router.post('/auth', async (req, res) => {

  var username = req.body.username;
  var vorname = req.body.vorname;
  var nachname = req.body.nachname;
  var email = req.body.email;
  var passwort = req.body.password;
  var hash = crypto.createHash('md5').update(passwort).digest("hex");

  console.log(username + " " + vorname + " " + nachname + " " + email + " " + passwort + " " + hash);


  try {
    const pool = await poolPromise;
    const result = await pool.request()
        .input('username', sql.VarChar(50), username)
        .input('email', sql.VarChar(50), email)
        .query('select id from Nutzer where username = @username or email = @email');
        
        if (result.rowsAffected < 1) {
          //user noch nicht vorhanden -> einfügen -> einloggen
          const pool_register = await poolPromise;
          const result_register = await pool_register.request()
              .input('username', sql.VarChar(50), username)
              .input('vorname', sql.VarChar(50), vorname)
              .input('nachname', sql.VarChar(50), nachname)
              .input('email', sql.VarChar(50), email)
              .input('hash', sql.VarChar(50), hash)
              .query('insert into nutzer (username, name, vorname, email, passwort_hash) values (@username, @nachname, @vorname, @email, @hash)');

          const pool_login = await poolPromise;
          const result_login = await pool_login.request()
              .input('username', sql.VarChar(50), username)
              .input('vorname', sql.VarChar(50), vorname)
              .input('nachname', sql.VarChar(50), nachname)
              .input('email', sql.VarChar(50), email)
              .input('hash', sql.VarChar(50), hash)
              .query('select id from Nutzer where username = @username');

          var nutzer_id = result_login.recordset[0].id;
          res.cookie('nutzer_id', nutzer_id);
          res.redirect('../');

          console.log("registriert");
        }else{
          console.log("bereits vorhanden")
          res.redirect('/register?vergeben=1');
        }
  } catch (err) {
    res.status(500)
    res.send(err.message)
  }
})









module.exports = router;
