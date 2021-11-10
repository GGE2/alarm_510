var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');
var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.listen(3000, 'localhost', function () {
    console.log('connection server........');
});

var connection = mysql.createConnection({
    host: "admindatabase.cpb2wh1r8lsd.us-east-2.rds.amazonaws.com",
    user: "GGE2",
    database: "DB_ADMIN",
    password: "administer",
    port: 3306
});

app.post('/user/join', function (req, res) {
    console.log(req.body);
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var userName = req.body.userName;

    // ������ �����ϴ� sql��.
    var sql = 'INSERT INTO Users (UserEmail, UserPwd, UserName) VALUES (?, ?, ?)';
    var params = [userEmail, userPwd, userName];

    // sql ���� ?�� �ι�° �Ű������� �Ѱ��� params�� ������ ġȯ�ȴ�.
    connection.query(sql, params, function (err, result) {
        var resultCode = 404;
        var message = '������ �߻��߽��ϴ�';

        if (err) {
            console.log(err);
        } else {
            resultCode = 200;
            message = 'ȸ�����Կ� �����߽��ϴ�.';
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    });
});

app.post('/user/login', function (req, res) {
    var userEmail = req.body.userEmail;
    var userPwd = req.body.userPwd;
    var sql = 'select * from Users where UserEmail = ?';

    connection.query(sql, userEmail, function (err, result) {
        var resultCode = 404;
        var message = '������ �߻��߽��ϴ�';

        if (err) {
            console.log(err);
        } else {
            if (result.length === 0) {
                resultCode = 204;
                message = '�������� �ʴ� �����Դϴ�!';
            } else if (userPwd !== result[0].UserPwd) {
                resultCode = 204;
                message = '��й�ȣ�� Ʋ�Ƚ��ϴ�!';
            } else {
                resultCode = 200;
                message = '�α��� ����! ' + result[0].UserName + '�� ȯ���մϴ�!';
            }
        }

        res.json({
            'code': resultCode,
            'message': message
        });
    })
});


