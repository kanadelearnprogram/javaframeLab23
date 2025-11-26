<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加学生</title>
</head>
<body>
    <!-- 表单提交路径：需匹配 Controller 的映射（含上下文路径） -->
    <!-- 若上下文路径是 /javaframeLab23_war_exploded，action 需写完整路径 -->
    <form action="${pageContext.request.contextPath}/student/add" method="post">
        <div>
            学生ID：<input type="text" name="studentId" required> <!-- name="studentId"  对应 DTO.studentId -->
        </div>
        <div>
            学生姓名：<input type="text" name="studentName" required> <!-- name="studentName"  对应 DTO.studentName -->
        </div>
        <div>
            所属班级：<input type="text" name="className" required> <!-- name="className"  对应 DTO.className -->
        </div>
        <div>
            联系电话：<input type="text" name="phone"> <!-- name="phone"  对应 DTO.phone -->
        </div>
        <div>
            性别：
            <select name="gender" required> <!-- name="gender"  对应 DTO.gender -->
                <option value="男">男</option>
                <option value="女">女</option>
            </select>
        </div>
        <div>
            状态：
            <select name="status" required>
                <option value="normal">正常</option>
                <option value="retake">重修</option>
            </select>
        </div>
        <button type="submit">提交</button>
    </form>
</body>
</html>