var express = require('express');
var router = express.Router();
var path = require('path');
const crypto = require('crypto');
var cookieParser = require('cookie-parser');



// GET route for reading data
router.get('/', function (req, res, next) {
  res.sendFile(path.join(__dirname + '/dsgvo.html'));
});








module.exports = router;
