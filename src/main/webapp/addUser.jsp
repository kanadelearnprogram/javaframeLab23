<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加用户</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input[type="text"], input[type="password"], input[type="date"], select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        .btn {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .message {
            padding: 10px;
            margin: 10px 0;
            border-radius: 4px;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>添加用户</h2>
        
        <% 
            String message = (String) request.getAttribute("message");
            String messageType = (String) request.getAttribute("messageType");
            if (message != null) {
        %>
            <div class="message <%= messageType %>">
                <%= message %>
            </div>
        <% } %>
        
        <form action="addUser" method="post">
            <div class="form-group">
                <label for="userId">用户ID:</label>
                <input type="text" id="userId" name="userId" required>
            </div>
            
            <div class="form-group">
                <label for="username">用户名:</label>
                <input type="text" id="username" name="username" required>
            </div>
            
            <div class="form-group">
                <label for="password">密码:</label>
                <input type="password" id="password" name="password" required>
            </div>
            
            <div class="form-group">
                <label for="role">角色:</label>
                <select id="role" name="role">
                    <option value="admin">管理员</option>
                    <option value="edu_admin">教务</option>
                    <option value="teacher">教师</option>
                    <option value="student">学生</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="realName">真实姓名:</label>
                <input type="text" id="realName" name="realName" required>
            </div>
            
            <div class="form-group">
                <label for="gender">性别:</label>
                <select id="gender" name="gender">
                    <option value="男">男</option>
                    <option value="女">女</option>
                </select>
            </div>
            
            <div class="form-group">
                <label for="registerDate">注册日期:</label>
                <input type="date" id="registerDate" name="registerDate" required>
            </div>
            
            <div class="form-group">
                <label for="phone">联系方式:</label>
                <input type="text" id="phone" name="phone">
            </div>
            
            <button type="submit" class="btn">添加用户</button>
        </form>
    </div>
</body>
</html>