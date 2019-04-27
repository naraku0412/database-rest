var express = require('express'); 
var http = require('http'); 
var app = express();
var url = require("url");
var port = 8888; 
var callfile = require('child_process'); 
var fs = require('fs');
var cors = require('cors');

http.createServer(app).listen(port); 
var bodyParser = require('body-parser');
var jsonParser = bodyParser.json()

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: false}));
app.all("*",function(req,res,next){ 
        res.header("Access-Control-Allow-Origin","*"); 
        res.header("Access-Control-Allow-Headers","content-type"); 
        res.header("Access-Control-Allow-Methods","DELETE,PUT,POST,GET,OPTIONS"); 
          if (req.method.toLowerCase() == 'options') 
               res.send(200); 
          else 
               next(); 
});
app.use(cors({
    origin:['http://localhost:8088'],
    methods:['GET','POST'],
    //alloweHeaders:['Conten-Type', 'Authorization']
    //alloweHeaders:['Conten-Type', 'application/json']
    alloweHeaders:['Conten-Type', 'application/x-www-form-urlencoded']
}));
app.get('/', function (req, res) {
   res.send('Hello World');
});

app.post('/api/snap/setRealTime',function(req,res){
         var body = req.body;
         var key = JSON.stringify(body);
         console.log("key:");
         console.log(key);
         callfile.execFile('/execfile/AppSetReal.sh',['-k',key],null,function (error, stdout, stderr) {
            if(error){
            console.log(error);
            res.send(error);
            }else if(stderr){
            console.log(stderr);
            res.send(stderr);
            }else if(stdout){
            console.log(stdout);
            res.send(stdout);}
       });
});


app.get('/api/snap/getModelDef',function(req,res){
         callfile.execFile('/workspace/modelget.sh',null,function (error, stdout, stderr) {
            console.log(stdout);
            res.send(stdout);
       });
}); 
app.get('/api/snap/setModelDef',function(req,res){
         callfile.execFile('/workspace/modelset.sh',null,function (error, stdout, stderr) {
            console.log(stdout);
            res.send(stdout);
       });
}); 
app.post('/api/snap/getRealtimeYC',function(req,res){
         var body = req.body;
         var key = JSON.stringify(body);
         console.log("key:");
         console.log(key);
         callfile.execFile('/execfile/GetRealtimeYCData.sh',['-k',key],null,function (error, stdout, stderr) {
            if(error){
            console.log(error);
            res.send(error);
            }else if(stderr){
            console.log(stderr);
            res.send(stderr);
            }else if(stdout){
            console.log(stdout);
            res.send(stdout);}
       });
});
app.post('/api/snap/GetDayYCData',function(req,res){
         var body = req.body;
         var key = JSON.stringify(body);
         console.log("key:");
         console.log(key);
         callfile.execFile('/execfile/GetDayYCData.sh',['-k',key],null,function (error, stdout, stderr) {
            if(error){
            console.log(error);
            res.send(error);
            }else if(stderr){
            console.log(stderr);
            res.send(stderr);
            }else if(stdout){
            console.log(stdout);
            res.send(stdout);}
       });
});
app.post('/api/snap/GetPeroidVarNum',function(req,res){
         var body = req.body;
         var key = JSON.stringify(body);
         console.log("key:");
         console.log(key);
         callfile.execFile('/execfile/GetPeroidVarNum.sh',['-k',key],null,function (error, stdout, stderr) {
            if(error){
            console.log(error);
            res.send(error);
            }else if(stderr){
            console.log(stderr);
            res.send(stderr);
            }else if(stdout){
            console.log(stdout);
            res.send(stdout);}
       });
});
app.post('/api/snap/GetPeriodYCData',function(req,res){
         var body = req.body;
         var key = JSON.stringify(body);
         console.log("key:");
         console.log(key);
         callfile.execFile('/execfile/GetPeriodYCData.sh',['-k',key],null,function (error, stdout, stderr) {
            if(error){
            console.log(error);
            res.send(error);
            }else if(stderr){
            console.log(stderr);
            res.send(stderr);
            }else if(stdout){
            console.log(stdout);
            res.send(stdout);}
       });
});
