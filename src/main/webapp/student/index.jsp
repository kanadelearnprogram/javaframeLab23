<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>学生个人中心</title>
    <style>
        body {
            margin: 0;
            font-family: "Microsoft YaHei", Arial, sans-serif;
            background: #f3f5f9;
        }
        header {
            background: #2b5cff;
            color: #fff;
            padding: 20px 40px;
        }
        .container {
            padding: 30px 40px;
        }
        .section {
            background: #fff;
            border-radius: 12px;
            padding: 22px;
            margin-bottom: 20px;
            box-shadow: 0 10px 24px rgba(0,0,0,0.06);
        }
        .section h2 {
            margin: 0 0 10px;
            font-size: 20px;
        }
        .section p {
            margin: 0 0 8px;
            color: #4e5969;
        }
        code {
            background: #f2f3f5;
            padding: 2px 6px;
            border-radius: 4px;
            font-size: 13px;
        }
    </style>
</head>
<body>
<header>
    <h1>学生个人中心</h1>
    <p>查看已选课程、成绩与个人信息</p>
</header>
<div class="container">
    <div class="section">
        <h2>已选课程</h2>
        <p>接口：<code>GET /api/student/courses</code></p>
        <p>支持分页、按课程名称或教师名称排序；展示课程状态、占比、选课时间等。</p>
    </div>
    <div class="section">
        <h2>成绩查询</h2>
        <p>接口：<code>GET /api/student/scores</code></p>
        <p>默认按总成绩降序，可切换平时/期末/更新时间排序；展示是否及格及占比。</p>
    </div>
    <div class="section">
        <h2>个人信息</h2>
        <p>后续可补充个人资料修改、密码修改等入口。</p>
    </div>
</div>
</body>
</html>

