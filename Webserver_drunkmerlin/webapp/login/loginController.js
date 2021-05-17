var express = require('express');
var router = express.Router();
var path = require('path');
const crypto = require('crypto');
var cookieParser = require('cookie-parser');

var sql = require("mssql");

const { poolPromise } = require('../db.js')



// GET route for reading data
router.get('/', function (req, res, next) {
  res.sendFile(path.join(__dirname + '/login.html'));
});

router.post('/auth', async (req, res) => {
  var username = req.body.username;
  var password = req.body.password;
  var hash = crypto.createHash('md5').update(password).digest("hex");

  try {
    const pool = await poolPromise
    const result = await pool.request()
        .input('username', sql.VarChar(50), username)
        .input('hash', sql.VarChar(50), hash)
        .query('select id from Nutzer where username = @username and passwort_hash = @hash');
        
    //console.log(result.recordset);

    if (result.rowsAffected < 1) {
        //kein ergebniss
        res.redirect('/login?loginfailed=1');
        //res.send("Login gescheitert");
      }else{
        var nutzer_id = result.recordset[0].id;

        console.log(nutzer_id);

        //res.send('login ' + nutzer_id);

        res.cookie('nutzer_id', nutzer_id);
        //res.send(req.cookies.nutzer_id);
        res.redirect('../');
      }
  } catch (err) {
    res.status(500)
    res.send(err.message)
  }
});

router.get('../register', function (req, res, next) {
  console.log(config.db);
});

router.get('/logout', function(req, res, next){
  res.clearCookie("nutzer_id");
  res.redirect('/login');
})



module.exports = router;
