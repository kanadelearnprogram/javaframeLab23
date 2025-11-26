<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - 首页</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .nav-links {
            text-align: center;
            margin: 30px 0;
        }
        .nav-links a {
            display: inline-block;
            margin: 10px;
            padding: 10px 20px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .nav-links a:hover {
            background-color: #0056b3;
        }
        .welcome {
            text-align: center;
            font-size: 18px;
            color: #666;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>教务管理系统</h1>
        
        <div class="welcome">
            欢迎使用教务管理系统
        </div>
        
        <div class="nav-links">
            <a href="login">用户登录</a>
            <a href="addStudent.jsp">添加学生</a>
            <a href="addUser.jsp">添加用户</a>
        </div>
        
        <div style="text-align: center; margin-top: 30px; color: #999;">
            <p>请选择您要进行的操作</p>
        </div>
    </div>
</body>
</html>