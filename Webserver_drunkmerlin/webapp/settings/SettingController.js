var express = require('express');
var router = express.Router();
var path = require('path');
const crypto = require('crypto');
var cookieParser = require('cookie-parser');



// GET route for reading data
router.get('/', function (req, res, next) {
  //res.sendFile(path.join(__dirname + '/setting.html'));

    var gewicht = req.cookies.gewicht;
    var sex = req.cookies.sex;

    if((gewicht === undefined) || (sex === undefined)){
        var male = 'checked';
        var female = '';
        var gewicht = '';
        res.render(__dirname + '/setting.html', {gewicht:gewicht,male:male, female:female});    
    }else{
        if(sex == "M"){
            var male = "checked";
        }else{
            var male="";
        }
    
    
        if(sex == "W"){
            var female = "checked";
        }else{
            var female = "";
        }
    
        console.log("gewicht: " + gewicht + "   sex: " + sex);
        res.render(__dirname + '/setting.html', {gewicht:gewicht,male:male, female:female});    
    
    }

    
    
});

router.post('/safe', function(req, res, next){
    var gewicht = req.body.gewicht;
    var sex = req.body.sex;

    console.log("gewicht: " + gewicht + "  sex: " + sex);
    

    res.cookie('gewicht', gewicht);
    res.cookie('sex', sex);

    res.redirect('/');

    
});






module.exports = router;
