var express = require('express');
var router = express.Router();
var path = require('path');
const crypto = require('crypto');
var cookieParser = require('cookie-parser');



// GET route for reading data
router.get('/', function (req, res, next) {
    var nutzer_id = req.cookies.nutzer_id;

    if(nutzer_id === undefined){
        res.redirect('/login');
        return;
    }


  res.sendFile(path.join(__dirname + '/scoreboard.html'));
});








module.exports = router;
