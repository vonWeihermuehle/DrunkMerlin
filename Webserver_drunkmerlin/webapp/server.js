var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');
var session = require('express-session');
var cookieParser = require('cookie-parser');


const port = 8080;


var loginRouter = require('./login/loginController.js');
var SallyRouter = require('./sally/controller.js');
var RegisterRouter = require('./register/registerController.js');
var AboutRouter = require('./about/AboutController.js');
var DsgvoRouter = require('./datenschutz/dsgvoController.js');
var SettingRouter = require('./settings/SettingController.js');
var ResetRouter = require('./reset/resetController.js');
var ScoreboardRouter = require('./scoreboard/ScoreboardController.js');

var app = express();

app.use(bodyParser.urlencoded({extended : true}));
app.use(bodyParser.json());

app.use(cookieParser());

app.engine('html', require('ejs').renderFile);




//app.use(express.static('public'));
app.use('/static', express.static(__dirname + '/public'));

app.use('/login', loginRouter);
app.use('/sally', SallyRouter);
app.use('/register', RegisterRouter);
app.use('/about', AboutRouter);
app.use('/dsgvo', DsgvoRouter);
app.use('/setting', SettingRouter);
app.use('/reset', ResetRouter);
app.use('/scoreboard', ScoreboardRouter);

app.get('/', function(request, response) {
	var nutzer_id = request.cookies.nutzer_id;

	//console.log(nutzer_id);

	if(nutzer_id === undefined){
		response.redirect('/login');
	}else{
		//response.send('Welcome back, ' + nutzer_id + '!');
		response.redirect('/sally');
	}
	response.end();
});



app.listen(port);
console.log("Server listening on Port: " + port);
