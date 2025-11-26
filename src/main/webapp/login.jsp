<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户登录</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .login-form {
            background-color: white;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            width: 300px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"], input[type="password"] {
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
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .error-message {
            color: red;
            margin-top: 10px;
            text-align: center;
        }
        .mt-2 {
            margin-top: 20px;
            padding: 10px;
            background-color: #e9ecef;
            border-radius: 4px;
        }
        .text-center {
            text-align: center;
        }
        .mb-2 {
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <main class="main-content">
        <div class="login-form">
            <h2 class="text-center">用户登录</h2>
            <p class="text-center mb-2">请输入您的登录凭据</p>
            
            <form action="login" method="post">
                <div class="form-group">
                    <label for="username">用户名:</label>
                    <input type="text" id="username" name="username" required>
                </div>
                
                <div class="form-group">
                    <label for="password">密码:</label>
                    <input type="password" id="password" name="password" required>
                </div>
                
                <button type="submit" class="btn">登录</button>
            </form>
            
            <% 
                String error = request.getParameter("error");
                if ("invalid".equals(error)) {
            %>
                <div class="error-message">
                    用户名或密码错误!
                </div>
            <% } %>
            
            <div class="mt-2">
                <p><strong>提示:</strong> 使用以下凭据登录</p>
                <p>用户名: admin</p>
                <p>密码: 123456</p>
            </div>
        </div>
    </main>
</body>
</html>