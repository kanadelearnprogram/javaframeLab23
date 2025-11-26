<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>教师工作台</title>
    <style>
        body {
            margin: 0;
            font-family: "Microsoft YaHei", Arial, sans-serif;
            background: #eef2fb;
        }
        header {
            background: #1f5eff;
            color: #fff;
            padding: 20px 40px;
        }
        .container {
            padding: 30px 40px;
        }
        .panel {
            background: #fff;
            border-radius: 14px;
            padding: 24px;
            box-shadow: 0 10px 30px rgba(11,66,205,0.1);
            margin-bottom: 24px;
        }
        .panel h2 {
            margin: 0 0 12px;
            color: #1d2129;
        }
        .panel p {
            margin: 0 0 10px;
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
    <h1>教师工作台</h1>
    <p>授课班级、成绩管理、Excel 工具与统计一站式入口</p>
</header>
<div class="container">
    <div class="panel">
        <h2>授课班级列表</h2>
        <p>调用 <code>GET /api/teacher/classes</code> 拉取本人授课班级，点击后进入班级成绩管理。</p>
    </div>
    <div class="panel">
        <h2>班级成绩管理</h2>
        <p>查询接口：<code>GET /api/teacher/classes/{classId}/scores?courseId=</code></p>
        <p>单条编辑：<code>PUT /api/teacher/scores/{scoreId}</code> （平时/期末成绩 0-100 校验，自动计算总评与及格状态）。</p>
    </div>
    <div class="panel">
        <h2>Excel 工具</h2>
        <p>模板下载：<code>GET /api/teacher/scores/template?classId=&amp;courseId=</code></p>
        <p>批量导入：<code>POST /api/teacher/scores/import</code>（multipart，模板中预填学号与姓名）。</p>
        <p>导出成绩：<code>GET /api/teacher/classes/{classId}/courses/{courseId}/scores/export</code></p>
    </div>
    <div class="panel">
        <h2>成绩统计</h2>
        <p>接口：<code>GET /api/teacher/classes/{classId}/courses/{courseId}/stats</code>，提供人数、及格率、分数段与不及格名单。</p>
    </div>
</div>
</body>
</html>

