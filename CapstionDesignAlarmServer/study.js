const express = require('express');
const app = express();
app.use(express.json());
app.use(express.urlencoded({extended: false}))
const port = 8002
const database = require('mime-db');
var url = require('url')
var mysql = require('mysql');
const { BadGateway } = require('http-errors');
var db = mysql.createConnection({
    host: 'admindatabase.cpb2wh1r8lsd.us-east-2.rds.amazonaws.com',
    user: 'admin2',
    password: 'administer2!@',
    database: 'Capdesign_Alram_510',
    dateStrings : 'date',
});

app.post('/godelete', function(request,response){
    db.query('UPDATE solve SET answer_check = "true" WHERE user_id = "' + request.body.user_id+ '" AND problem_id = "' + request.body.problem_id + '"',
    function(error){
        if (error) { console.log(error); }
        response.send("삭제완료");
    });
});

app.get('/alramproblem', function(request,response) {
    var queryData = url.parse(request.url, true).query;
    if(queryData.category_id.length == 2){
        db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id WHERE category_id="' + queryData.category_id[0] + '" or category_id="'+ queryData.category_id[1] +'"',
            function (error, categoryId1) {
                if (error) { console.log(error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                converted_question.splice(random);
                var no_answers = []
                for (var i = 0; i < 3; i++) {
                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    no_answers.push(converted_question[no_answer].answer);
                    converted_question.splice(no_answer);
                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
    }else if(queryData.category_id.length == 3){
        db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id WHERE category_id="' + queryData.category_id[0] + '"or category_id="'+ queryData.category_id[1] +'"or category_id="'+ queryData.category_id[2] +'"',
            function (error, categoryId1) {
                if (error) { console.log(error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                converted_question.splice(random);
                var no_answers = []
                for (var i = 0; i < 3; i++) {
                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    no_answers.push(converted_question[no_answer].answer);
                    converted_question.splice(no_answer);
                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
    }else if(queryData.category_id.length == 4){
        db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id WHERE category_id="' + queryData.category_id[0] + '"or category_id="'+ queryData.category_id[1] +'"or category_id="'+ queryData.category_id[2] +'"or category_id="'+ queryData.category_id[3] +'"',
            function (error, categoryId1) {
                if (error) { console.log(error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                converted_question.splice(random);
                var no_answers = []
                for (var i = 0; i < 3; i++) {
                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    no_answers.push(converted_question[no_answer].answer);
                    converted_question.splice(no_answer);
                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
    }else if(queryData.category_id.length == 5){
        db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id',
            function (error, categoryId1) {
                if (error) { console.log( error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                converted_question.splice(random);
                var no_answers = []
                for (var i = 0; i < 3; i++) {
                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    no_answers.push(converted_question[no_answer].answer);
                    converted_question.splice(no_answer);
                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
    }else {
        if(queryData.category_id[0] == 6){
            db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id',
            function (error, categoryId1) {
                if (error) { console.log(error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                converted_question.splice(random);
                var no_answers = []
                for (var i = 0; i < 3; i++) {
                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    no_answers.push(converted_question[no_answer].answer);
                    converted_question.splice(no_answer);
                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
        }else{
            db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id WHERE category_id="' + queryData.category_id[0]+ '"',
            function (error, categoryId1) {
                if (error) { console.log(error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                converted_question.splice(random);
                var no_answers = []
                for (var i = 0; i < 3; i++) {
                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    no_answers.push(converted_question[no_answer].answer);
                    converted_question.splice(no_answer);
                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
        }
    }
});

app.get('/randomproblem', function (request, response) { 
    console.log('randomproblem!')
    var queryData = url.parse(request.url, true).query;
    if (queryData.category_id == 6) {
        db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id',
            function (error, categoryId1) {
                if (error) { console.log(error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                random_question_id = converted_question[random].category_id
                var no_answers = []
                converted_question.splice(random);
                for (var i = 0; no_answers.length < 3; i++) {

                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    if (converted_question[no_answer].category_id == random_question_id) {
                        no_answers.push(converted_question[no_answer].answer);
                        no_answers.splice(no_answer);
                    }

                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
    } else {
        db.query('SELECT question, answer, category_id, category_name, problem_id FROM problem INNER JOIN category ON category_idx = category_id WHERE category_id="' + queryData.category_id + '"',
            function (error, categoryId1) {
                if (error) { console.log(error()); }
                var converted_question = []
                var random_question = {}
                for (cg of categoryId1) {
                    converted_question.push({ 'question': cg.question, 'answer': cg.answer, 'category_id': cg.category_id, 'category_name': cg.category_name, 'problem_id': cg.problem_id })
                }
                var random = Math.floor(Math.random() * converted_question.length);
                random_question['question'] = converted_question[random].question
                random_question['answer'] = converted_question[random].answer
                random_question['problem_id'] = converted_question[random].problem_id
                converted_question.splice(random);
                var no_answers = []
                for (var i = 0; i < 3; i++) {
                    var no_answer = Math.floor(Math.random() * converted_question.length);
                    no_answers.push(converted_question[no_answer].answer);
                    converted_question.splice(no_answer);
                }
                random_question['no_answers'] = no_answers
                response.json(random_question);
            });
    }
});

app.post('/solve', function (request, response) { 
        db.query('SELECT * FROM solve WHERE user_id = "' + request.body.user_id + '" AND problem_id = "' + request.body.problem_id + '"',
            function (error, solve_list) {
                if (solve_list.length == 0) {
                    var query = 'INSERT INTO solve VALUES("' + request.body.user_id + '", "' + request.body.problem_id + '","' + request.body.answer_check + '", now())'
                    db.query(query, function (error) {
                        if (error) { console.log(error); }
                    })
                    
                } else {
                    db.query('UPDATE solve SET date_time = now(), answer_check = "' + request.body.answer_check + '" WHERE user_id = "' + request.body.user_id + '" AND problem_id = "' + request.body.problem_id + '"',
                        function (error) {
                            console.log("update");
                            if (error) { console.log(error); }
                        });
                }
            });
});

app.get('/getWrongProblems', function(request, response){
    var queryData = url.parse(request.url, true).query;
    
    db.query('SELECT * FROM solve INNER JOIN problem ON solve.problem_id = problem.problem_id INNER JOIN category ON category.category_id = problem.category_idx WHERE user_id ="'+ queryData.user_id +'" AND answer_check = "false"',
     function(error, user_data){
        user_wrong_data = []
        for(uwd of user_data){
            user_wrong_data.push({'date_time' : uwd.date_time , 'category_name' : uwd.category_name, 'problem_id' : uwd.problem_id, 'question' : uwd.question, 'user_uid' :uwd.user_id});
        }
            response.json(user_wrong_data);
    });
});

app.get('/getWrongEnglish', function(request, response){
    var queryData = url.parse(request.url, true).query;

    db.query('SELECT * FROM solve INNER JOIN problem ON solve.problem_id = problem.problem_id INNER JOIN category ON category.category_id = problem.category_idx WHERE user_id ="'+ queryData.user_id +'" AND answer_check = "false" AND category_id = "1"',
    function(error, user_data){
        user_wrong_data = []
        for(uwd of user_data){
            user_wrong_data.push({'date_time' : uwd.date_time , 'category_name' : uwd.category_name, 'problem_id' : uwd.problem_id, 'question' : uwd.question, 'user_uid' :uwd.user_id});
        }
        response.json(user_wrong_data);
    });
});

app.get('/getWrongCapital', function(request, response){
    var queryData = url.parse(request.url, true).query;

    db.query('SELECT * FROM solve INNER JOIN problem ON solve.problem_id = problem.problem_id INNER JOIN category ON category.category_id = problem.category_idx WHERE user_id ="'+ queryData.user_id +'" AND answer_check = "false" AND category_id = "2"',
    function(error, user_data){
        user_wrong_data = []
        for(uwd of user_data){
            user_wrong_data.push({'date_time' : uwd.date_time , 'category_name' : uwd.category_name, 'problem_id' : uwd.problem_id, 'question' : uwd.question, 'user_uid' :uwd.user_id});
        }
        response.json(user_wrong_data);
    });
});

app.get('/getWrongProverb', function(request, response){
    var queryData = url.parse(request.url, true).query;

    db.query('SELECT * FROM solve INNER JOIN problem ON solve.problem_id = problem.problem_id INNER JOIN category ON category.category_id = problem.category_idx WHERE user_id ="'+ queryData.user_id +'" AND answer_check = "false" AND category_id = "3"',
    function(error, user_data){
        user_wrong_data = []
        for(uwd of user_data){
            user_wrong_data.push({'date_time' : uwd.date_time , 'category_name' : uwd.category_name, 'problem_id' : uwd.problem_id, 'question' : uwd.question, 'user_uid' :uwd.user_id});
        }
        response.json(user_wrong_data);
    });
});

app.get('/getWrongIdiom', function(request, response){
    var queryData = url.parse(request.url, true).query;

    db.query('SELECT * FROM solve INNER JOIN problem ON solve.problem_id = problem.problem_id INNER JOIN category ON category.category_id = problem.category_idx WHERE user_id ="'+ queryData.user_id +'" AND answer_check = "false" AND category_id = "4"',
    function(error, user_data){
        user_wrong_data = []
        for(uwd of user_data){
            user_wrong_data.push({'date_time' : uwd.date_time , 'category_name' : uwd.category_name, 'problem_id' : uwd.problem_id, 'question' : uwd.question, 'user_uid' :uwd.user_id});
        }
        response.json(user_wrong_data);
    });
});

app.get('/getWrongHistory', function(request, response){
    var queryData = url.parse(request.url, true).query;

    db.query('SELECT * FROM solve INNER JOIN problem ON solve.problem_id = problem.problem_id INNER JOIN category ON category.category_id = problem.category_idx WHERE user_id ="'+ queryData.user_id +'" AND answer_check = "false" AND category_id = "5"',
    function(error, user_data){
        user_wrong_data = []
        for(uwd of user_data){
            user_wrong_data.push({'date_time' : uwd.date_time , 'category_name' : uwd.category_name, 'problem_id' : uwd.problem_id, 'question' : uwd.question, 'user_uid' :uwd.user_id});
        }
        response.json(user_wrong_data);
    });
});


app.get('/wrong_problem', function (request, response) {
    var queryData = url.parse(request.url, true).query;
    db.query('SELECT * FROM problem INNER JOIN category ON category.category_id = problem.category_idx WHERE problem_id = "'+queryData.problem_id+'"',function(error, wrong_data){
        if(error){ console.log(error);}
        wrong_question = {}
        wrong_question['question'] = wrong_data[0].question;
        wrong_question['answer'] = wrong_data[0].answer;
        wrong_question['category_name'] = wrong_data[0].category_name;
        wrong_question['problem_id'] = wrong_data[0].problem_id;
        var no_answers = []
        
        db.query('SELECT answer from problem WHERE category_idx= "'+wrong_data[0].category_id+'"', function(error, answers){
            for(var j = 0; j < 3 ; j++){
                var randomint = Math.floor(Math.random() * answers.length);
                no_answers.push(answers[randomint].answer);
                answers.splice(randomint);
            }
            wrong_question['no_answers'] = no_answers
            response.json(wrong_question);
        });
    });
});

app.listen(port, function () {

});