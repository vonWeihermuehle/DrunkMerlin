var express = require('express');
var router = express.Router();
var path = require('path');
const crypto = require('crypto');
var cookieParser = require('cookie-parser');

var sql = require("mssql");

const { poolPromise } = require('../db.js')


router.get('/drink', async (req, res) => {
  var nutzer_id = req.cookies.nutzer_id;


  try {
    const pool = await poolPromise
    const result = await pool.request()
        .input('nutzer_id', sql.VarChar(50), nutzer_id)
        .query('delete from drink where nutzer_id = @nutzer_id');   

    res.clearCookie("items");
    res.redirect('../');
  } catch (err) {
    res.status(500)
    res.send(err.message)
  }
})










module.exports = router;
