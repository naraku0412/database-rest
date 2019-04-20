var express = require('express'); 
var http = require('http'); 
var app = express();
var url = require("url");
var port = 8888; 
var callfile = require('child_process'); 
var fs = require('fs');
var cors = require('cors');

http.createServer(app).listen(port); 
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
    alloweHeaders:['Conten-Type', 'Authorization']
}));
app.get('/', function (req, res) {
   res.send('Hello World');
})
app.get('/api/snap/getModelDef',function(req,res){ 
        // var url_parts = url.parse(req.url,true); 
        // var query = url_parts.query; 
         var key = "getmodel";
         var ip  = "192.168.100.166";
         var jarPath = "/home/sherry/project-getModelDef.jar com.app.AppGet";
         
         //res.send('the input key: ' + key );
         //callfile.execFile('java',['-cp',jarPath],null,function (error, stdout, stderr) {
           //callfile.execFile('java -version',null,function (error, stdout, stderr) {
         callfile.execFile('/home/sherry/database-data-defmodel/getmodel.sh',null,function (error, stdout, stderr) {
           if(error){
            console.log(error);
            }else if(stderr){
            console.log(stderr);
            }else if(stdout){
           // console.log(stdout);
            res.send(stdout);}
       });
}); 
app.get('/api/snap/post',function(req,res){ 
         var url_parts = url.parse(req.url,true); 
        // console.log(url_parts);
         var query = url_parts.query; 
         ///console.log(query);
         var key = query.key;
         var value = query.value;
         //res.send('the input key: ' + key );
         //res.send('the input value: ' + value );
         callfile.execFile('/usr/local/bin/put',['-k',key,'-v',value],null,function (error, stdout, stderr) {
              if (error !== null) {
                  console.log('exec error: ' + error);
               }
         console.log('exec result: ' + stdout); 
         res.send("the input process is completed!" +stdout);
           });
});
