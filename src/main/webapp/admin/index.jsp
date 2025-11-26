<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>系统管理首页</title>
    <style>
        body {
            font-family: "Microsoft YaHei", Arial, sans-serif;
            margin: 0;
            background: #f5f7fb;
            color: #1d2129;
        }
        header {
            background: #0052d9;
            color: #fff;
            padding: 20px 40px;
        }
        .container {
            padding: 30px 40px;
        }
        .card-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
            margin-top: 25px;
        }
        .card {
            background: #fff;
            border-radius: 12px;
            padding: 20px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.05);
        }
        .card h3 {
            margin: 0 0 10px;
            font-size: 18px;
        }
        .card ul {
            padding-left: 18px;
            margin: 0;
        }
    </style>
</head>
<body>
<header>
    <h1>系统管理首页</h1>
    <p>当前角色：管理员</p>
</header>
<div class="container">
    <div class="card-grid">
        <div class="card">
            <h3>全局配置</h3>
            <ul>
                <li>系统公告 / 业务开关</li>
                <li>数据备份与恢复</li>
                <li>日志审计</li>
            </ul>
        </div>
        <div class="card">
            <h3>角色权限管理</h3>
            <ul>
                <li>角色新增 / 授权</li>
                <li>菜单 / 接口权限配置</li>
                <li>教师 / 学生账号审核</li>
            </ul>
        </div>
        <div class="card">
            <h3>模块配置入口</h3>
            <ul>
                <li>班级 / 学生 / 课程</li>
                <li>文件分类 / 空间策略</li>
                <li>指标/模板字典</li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>

