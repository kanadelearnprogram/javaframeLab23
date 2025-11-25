# 实验二：教务管理+个人空间文件管理 完善后接口文档
## 接口设计规范
1. **响应格式统一**：所有接口返回JSON格式，包含状态码、消息、数据三部分
```json
{
  "code": 200,          // 200=成功，400=参数错误，401=未登录，403=无权限，500=服务器错误
  "msg": "操作成功",     // 提示信息
  "data": {}            // 业务数据（查询类返回结果，操作类返回影响行数等）
}
```
2. **分页参数统一**：所有分页查询接口使用`page`（当前页码，默认1）、`limit`（每页条数，默认10）
3. **日期格式统一**：请求/响应中日期使用`yyyy-MM-dd`格式，时间使用`yyyy-MM-dd HH:mm:ss`格式
4. **编码规范**：URL使用小驼峰，参数名与数据库字段一致（下划线转小驼峰），枚举值使用英文小写（如`normal`/`pending`）

## 一、教务管理模块接口
### 1. 班级管理接口
#### 1.1 单条添加班级
- **请求方式**：POST
- **URL**：`/api/class/add`
- **请求体（Body）**：
```json
{
  "className": "软件工程2301",  // 班级名称（必填）
  "department": "计算机学院",   // 所属院系（必填）
  "teacherId": "T001"          // 班主任ID（可选，关联user表教师）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "班级添加成功",
  "data": {
    "classId": 1,
    "className": "软件工程2301",
    "department": "计算机学院",
    "teacherId": "T001",
    "createTime": "2025-01-05 10:30:00"
  }
}
```
- **业务约束**：班级名称唯一，重复添加返回`code=400`，提示“班级名称已存在”

#### 1.2 批量添加班级
- **请求方式**：POST
- **URL**：`/api/class/batch-add`
- **请求体（Body）**：
```json
[
  {
    "className": "软件工程2301",
    "department": "计算机学院",
    "teacherId": "T001"
  },
  {
    "className": "软件工程2302",
    "department": "计算机学院",
    "teacherId": "T001"
  }
]
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "批量添加成功",
  "data": {
    "successCount": 2,  // 成功添加数量
    "failCount": 0,     // 失败数量
    "failList": []      // 失败的班级名称及原因
  }
}
```

#### 1.3 查询班级列表
- **请求方式**：GET
- **URL**：`/api/class/list`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | page         | int     | 当前页码（默认1）             |
  | limit        | int     | 每页条数（默认10）            |
  | department   | string  | 所属院系（可选，模糊查询）    |
  | teacherId    | string  | 班主任ID（可选）              |
  | className    | string  | 班级名称（可选，模糊查询）    |
- **响应示例**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 2,          // 总记录数
    "pages": 1,          // 总页数
    "current": 1,        // 当前页码
    "size": 10,          // 每页条数
    "records": [
      {
        "classId": 1,
        "className": "软件工程2301",
        "department": "计算机学院",
        "teacherId": "T001",
        "teacherName": "李老师",  // 关联查询教师姓名
        "createTime": "2025-01-05 10:30:00"
      },
      {
        "classId": 2,
        "className": "软件工程2302",
        "department": "计算机学院",
        "teacherId": "T001",
        "teacherName": "李老师",
        "createTime": "2025-01-05 10:35:00"
      }
    ]
  }
}
```

#### 1.4 批量删除班级
- **请求方式**：POST
- **URL**：`/api/class/batch-delete`
- **请求体（Body）**：
```json
{
  "classIds": [1, 2]  // 要删除的班级ID列表（必填）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "批量删除成功",
  "data": {
    "successCount": 2,
    "failCount": 0,
    "failList": []
  }
}
```
- **业务约束**：若班级下有关联学生或课程，删除失败，返回`failList`说明“班级已关联学生，无法删除”

### 2. 学生管理接口
#### 2.1 单条添加学生
- **请求方式**：POST
- **URL**：`/api/student/add`
- **请求体（Body）**：
```json
{
  "studentId": "2023001",  // 学号（主键，必填）
  "userId": "U2023001",    // 关联用户ID（必填，唯一）
  "classId": 1,            // 所属班级ID（必填）
  "status": "normal"       // 状态（normal/retake，默认normal）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "学生添加成功",
  "data": {
    "studentId": "2023001",
    "userId": "U2023001",
    "userName": "张三",     // 关联用户姓名
    "classId": 1,
    "className": "软件工程2301",  // 关联班级名称
    "status": "normal"
  }
}
```
- **业务约束**：学号唯一、userId唯一，重复则返回`400`错误

#### 2.2 批量添加学生
- **请求方式**：POST
- **URL**：`/api/student/batch-add`
- **请求体（Body）**：
```json
[
  {
    "studentId": "2023001",
    "userId": "U2023001",
    "classId": 1,
    "status": "normal"
  },
  {
    "studentId": "2023002",
    "userId": "U2023002",
    "classId": 1,
    "status": "retake"
  }
]
```
- **响应示例**：同班级批量添加

#### 2.3 查询学生列表
- **请求方式**：GET
- **URL**：`/api/student/list`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | page         | int     | 当前页码（默认1）             |
  | limit        | int     | 每页条数（默认10）            |
  | classId      | int     | 所属班级ID（可选）            |
  | studentId    | string  | 学号（可选，模糊查询）        |
  | userName     | string  | 学生姓名（可选，模糊查询）    |
  | status       | string  | 状态（normal/retake，可选）   |
- **响应示例**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 2,
    "pages": 1,
    "current": 1,
    "size": 10,
    "records": [
      {
        "studentId": "2023001",
        "userId": "U2023001",
        "userName": "张三",
        "gender": "男",
        "classId": 1,
        "className": "软件工程2301",
        "status": "normal"
      },
      {
        "studentId": "2023002",
        "userId": "U2023002",
        "userName": "李四",
        "gender": "女",
        "classId": 1,
        "className": "软件工程2301",
        "status": "retake"
      }
    ]
  }
}
```

#### 2.4 批量删除学生
- **请求方式**：POST
- **URL**：`/api/student/batch-delete`
- **请求体（Body）**：
```json
{
  "studentIds": ["2023001", "2023002"]  // 要删除的学号列表（必填）
}
```
- **响应示例**：同班级批量删除
- **业务约束**：若学生有关联成绩，删除失败，返回`failList`说明“学生已关联成绩，无法删除”

### 3. 课程管理接口
#### 3.1 单条添加课程
- **请求方式**：POST
- **URL**：`/api/course/add`
- **请求体（Body）**：
```json
{
  "courseName": "Java程序设计",  // 课程名称（必填）
  "dailyRatio": 0.3,            // 平时成绩占比（必填，0-1之间）
  "examRatio": 0.7,             // 期末成绩占比（必填，0-1之间）
  "classId": 1,                 // 关联班级ID（必填）
  "teacherId": "T001"           // 授课教师ID（必填）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "课程添加成功",
  "data": {
    "courseId": 1,
    "courseName": "Java程序设计",
    "dailyRatio": 0.3,
    "examRatio": 0.7,
    "classId": 1,
    "className": "软件工程2301",
    "teacherId": "T001",
    "teacherName": "李老师"
  }
}
```
- **业务约束**：课程名称唯一；`dailyRatio + examRatio = 1.0`，不满足则返回`400`提示“权重之和必须为1.0”

#### 3.2 批量添加课程
- **请求方式**：POST
- **URL**：`/api/course/batch-add`
- **请求体（Body）**：
```json
[
  {
    "courseName": "Java程序设计",
    "dailyRatio": 0.3,
    "examRatio": 0.7,
    "classId": 1,
    "teacherId": "T001"
  },
  {
    "courseName": "数据库原理",
    "dailyRatio": 0.4,
    "examRatio": 0.6,
    "classId": 1,
    "teacherId": "T001"
  }
]
```
- **响应示例**：同班级批量添加

#### 3.3 查询课程列表
- **请求方式**：GET
- **URL**：`/api/course/list`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | page         | int     | 当前页码（默认1）             |
  | limit        | int     | 每页条数（默认10）            |
  | classId      | int     | 关联班级ID（可选）            |
  | teacherId    | string  | 授课教师ID（可选）            |
  | courseName   | string  | 课程名称（可选，模糊查询）    |
- **响应示例**：类似学生列表查询，包含课程基本信息及关联班级/教师名称

#### 3.4 批量删除课程
- **请求方式**：POST
- **URL**：`/api/course/batch-delete`
- **请求体（Body）**：
```json
{
  "courseIds": [1, 2]  // 要删除的课程ID列表（必填）
}
```
- **响应示例**：同班级批量删除
- **业务约束**：若课程有关联成绩，删除失败，返回`failList`说明“课程已关联成绩，无法删除”

### 4. 成绩管理接口
#### 4.1 单条保存成绩（录入/修改）
- **请求方式**：POST
- **URL**：`/api/score/save`
- **请求体（Body）**：
```json
{
  "studentId": "2023001",  // 学号（必填）
  "courseId": 1,            // 课程ID（必填）
  "dailyScore": 85.0,       // 平时成绩（必填，0-100）
  "examScore": 90.0         // 期末成绩（必填，0-100）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "成绩保存成功",
  "data": {
    "scoreId": 1,
    "studentId": "2023001",
    "studentName": "张三",
    "courseId": 1,
    "courseName": "Java程序设计",
    "dailyScore": 85.0,
    "examScore": 90.0,
    "dailyRatio": 0.3,
    "examRatio": 0.7,
    "totalScore": 88.5,
    "isPass": 1,
    "inputTime": "2025-01-05 11:00:00"
  }
}
```
- **业务逻辑**：自动计算总评成绩、及格状态；重修学生总评>75时修正为75

#### 4.2 批量保存成绩
- **请求方式**：POST
- **URL**：`/api/score/batch-save`
- **请求体（Body）**：
```json
[
  {
    "studentId": "2023001",
    "courseId": 1,
    "dailyScore": 85.0,
    "examScore": 90.0
  },
  {
    "studentId": "2023002",
    "courseId": 1,
    "dailyScore": 50.0,
    "examScore": 55.0
  }
]
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "批量保存成功",
  "data": {
    "successCount": 2,
    "failCount": 0,
    "failList": [],
    "records": [
      {
        "studentId": "2023001",
        "totalScore": 88.5,
        "isPass": 1
      },
      {
        "studentId": "2023002",
        "totalScore": 53.5,
        "isPass": 0
      }
    ]
  }
}
```

#### 4.3 成绩多维查询（含分页排序）
- **请求方式**：GET
- **URL**：`/api/score/list`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | page         | int     | 当前页码（默认1）             |
  | limit        | int     | 每页条数（默认10）            |
  | classId      | int     | 班级ID（可选）                |
  | courseId     | int     | 课程ID（可选）                |
  | studentId    | string  | 学号（可选，模糊查询）        |
  | userName     | string  | 学生姓名（可选，模糊查询）    |
  | isPass       | int     | 是否及格（0=不及格/1=及格，可选） |
  | sortField    | string  | 排序字段（totalScore/studentId/inputTime，默认totalScore） |
  | sortOrder    | string  | 排序方式（asc/desc，默认desc） |
- **响应示例**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 2,
    "pages": 1,
    "current": 1,
    "size": 10,
    "records": [
      {
        "scoreId": 1,
        "studentId": "2023001",
        "studentName": "张三",
        "gender": "男",
        "classId": 1,
        "className": "软件工程2301",
        "courseId": 1,
        "courseName": "Java程序设计",
        "dailyScore": 85.0,
        "examScore": 90.0,
        "totalScore": 88.5,
        "isPass": 1,
        "inputTime": "2025-01-05 11:00:00"
      },
      {
        "scoreId": 2,
        "studentId": "2023002",
        "studentName": "李四",
        "gender": "女",
        "classId": 1,
        "className": "软件工程2301",
        "courseId": 1,
        "courseName": "Java程序设计",
        "dailyScore": 50.0,
        "examScore": 55.0,
        "totalScore": 53.5,
        "isPass": 0,
        "inputTime": "2025-01-05 11:05:00"
      }
    ]
  }
}
```

#### 4.4 不及格统计接口
- **请求方式**：GET
- **URL**：`/api/score/fail-stat`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | classId      | int     | 班级ID（可选，为空查询所有班级） |
  | courseId     | int     | 课程ID（可选，为空查询所有课程） |
- **响应示例**：
```json
{
  "code": 200,
  "msg": "统计成功",
  "data": [
    {
      "classId": 1,
      "className": "软件工程2301",
      "courseId": 1,
      "courseName": "Java程序设计",
      "failCount": 1,          // 不及格人数
      "failStudents": "李四",  // 不及格学生姓名拼接
      "totalStudents": 2       // 该班级该课程总人数
    }
  ]
}
```

## 二、个人空间文件管理模块接口
### 1. 用户注册接口
#### 1.1 用户注册（含空间初始化）
- **请求方式**：POST
- **URL**：`/api/user/register`
- **请求体（Body）**：
```json
{
  "userId": "U2023001",      // 用户ID（工号/学号，必填，唯一）
  "username": "student1",    // 登录用户名（必填，唯一）
  "password": "123456",      // 登录密码（必填，后端BCrypt加密）
  "role": "student",         // 角色（admin/edu_admin/teacher/student，必填）
  "realName": "张三",        // 真实姓名（必填）
  "gender": "男",            // 性别（可选，默认未知）
  "registerDate": "2025-01-04",  // 注册日期（必填）
  "phone": "13600136000"     // 联系方式（可选）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "注册成功，已为您分配5M个人空间",
  "data": {
    "userId": "U2023001",
    "username": "student1",
    "role": "student",
    "realName": "张三",
    "registerDate": "2025-01-04",
    "spaceId": 1,
    "totalSize": 5242880,    // 5M字节
    "usedSize": 0
  }
}
```
- **业务逻辑**：注册成功后自动创建个人空间、服务器存储目录

### 2. 个人空间接口
#### 2.1 查询空间信息
- **请求方式**：GET
- **URL**：`/api/space/info`
- **查询参数（Query）**：`userId`（用户ID，必填）
- **响应示例**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "spaceId": 1,
    "userId": "U2023001",
    "totalSize": 5242880,    // 总空间5M
    "usedSize": 2097152,     // 已用2M
    "remainingSize": 3145728, // 剩余3M
    "applyStatus": "none",   // 扩展申请状态
    "applySize": 0,
    "approveDate": null
  }
}
```

#### 2.2 空间扩展申请
- **请求方式**：POST
- **URL**：`/api/space/apply`
- **请求体（Body）**：
```json
{
  "userId": "U2023001",      // 用户ID（必填）
  "applySize": 10485760      // 申请扩展大小（字节，必填，如10M=10*1024*1024）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "扩展申请提交成功，等待管理员审批",
  "data": {
    "spaceId": 1,
    "userId": "U2023001",
    "applyStatus": "pending",
    "applySize": 10485760
  }
}
```

#### 2.3 管理员审批空间扩展
- **请求方式**：POST
- **URL**：`/api/space/approve`
- **请求体（Body）**：
```json
{
  "userId": "U2023001",      // 用户ID（必填）
  "approveResult": "approved", // 审批结果（approved/rejected，必填）
  "approveAdminId": "ADMIN001", // 审批管理员ID（必填）
  "remark": "下载量达标，批准扩展10M" // 审批备注（可选）
}
```
- **响应示例**（审批通过）：
```json
{
  "code": 200,
  "msg": "审批通过，空间已扩展",
  "data": {
    "userId": "U2023001",
    "totalSize": 15728640,    // 原5M+扩展10M=15M
    "usedSize": 2097152,
    "applyStatus": "approved",
    "approveDate": "2025-01-06 09:30:00"
  }
}
```
- **响应示例**（审批拒绝）：
```json
{
  "code": 200,
  "msg": "审批拒绝",
  "data": {
    "userId": "U2023001",
    "applyStatus": "rejected",
    "approveDate": "2025-01-06 09:30:00",
    "remark": "下载量未达标，暂不批准"
  }
}
```

### 3. 文件管理接口
#### 3.1 文件上传（含空间校验）
- **请求方式**：POST
- **URL**：`/api/file/upload`
- **请求参数（FormData）**：
  | 参数名       | 类型        | 说明                          |
  |--------------|-------------|-------------------------------|
  | file         | File        | 文件二进制流（必填）          |
  | categoryId   | int         | 文件分类ID（1=文档/2=图片/3=相册/4=音乐，必填） |
  | fileName     | string      | 自定义文件名（可选，默认使用原文件名） |
- **请求头**：`Authorization: Bearer {token}`（登录令牌，必填）
- **响应示例**：
```json
{
  "code": 200,
  "msg": "文件上传成功，等待管理员审批",
  "data": {
    "fileId": 1,
    "fileName": "Java笔记.pdf",
    "filePath": "/upload/U2023001/Java笔记.pdf",
    "fileUrl": "http://localhost:8080/upload/U2023001/Java笔记.pdf",
    "fileSize": 2097152,       // 2M字节
    "fileType": "application/pdf",
    "categoryId": 1,
    "categoryName": "文档",
    "uploadTime": "2025-01-05 14:30:00",
    "status": "pending",
    "downloadCount": 0,
    "isTop": 0,
    "isApproved": 0
  }
}
```
- **业务约束**：文件大小+已用空间>总空间时，返回`400`提示“个人空间不足，无法上传”

#### 3.2 文件列表查询（个人/管理员）
- **请求方式**：GET
- **URL**：`/api/file/list`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | page         | int     | 当前页码（默认1）             |
  | limit        | int     | 每页条数（默认10）            |
  | userId       | string  | 上传用户ID（可选，管理员查询所有用户文件时使用） |
  | categoryId   | int     | 文件分类ID（可选）            |
  | fileName     | string  | 文件名（可选，模糊查询）      |
  | status       | string  | 文件状态（pending/normal/frozen，可选） |
  | isApproved   | int     | 是否审批通过（0/1，可选）     |
  | isTop        | int     | 是否置顶（0/1，可选）         |
  | sortField    | string  | 排序字段（uploadTime/downloadCount/fileSize，默认uploadTime） |
  | sortOrder    | string  | 排序方式（asc/desc，默认desc） |
- **响应示例**（个人用户查询）：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 1,
    "pages": 1,
    "current": 1,
    "size": 10,
    "records": [
      {
        "fileId": 1,
        "fileName": "Java笔记.pdf",
        "fileUrl": "http://localhost:8080/upload/U2023001/Java笔记.pdf",
        "fileSize": 2097152,
        "fileType": "application/pdf",
        "categoryId": 1,
        "categoryName": "文档",
        "uploadTime": "2025-01-05 14:30:00",
        "status": "pending",
        "downloadCount": 0,
        "isTop": 0,
        "isApproved": 0
      }
    ]
  }
}
```

#### 3.3 文件更新（名称/分类/置顶）
- **请求方式**：PUT
- **URL**：`/api/file/update`
- **请求体（Body）**：
```json
{
  "fileId": 1,               // 文件ID（必填）
  "fileName": "Java核心笔记.pdf", // 新文件名（可选）
  "categoryId": 1,           // 新分类ID（可选）
  "isTop": 1                 // 是否置顶（0/1，可选）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "文件更新成功",
  "data": {
    "fileId": 1,
    "fileName": "Java核心笔记.pdf",
    "categoryId": 1,
    "categoryName": "文档",
    "isTop": 1
  }
}
```

#### 3.4 文件冻结/解冻
- **请求方式**：PUT
- **URL**：`/api/file/status`
- **请求体（Body）**：
```json
{
  "fileId": 1,               // 文件ID（必填）
  "status": "frozen"         // 状态（frozen/normal，必填）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "文件冻结成功",
  "data": {
    "fileId": 1,
    "status": "frozen"
  }
}
```

#### 3.5 文件删除
- **请求方式**：DELETE
- **URL**：`/api/file/delete`
- **查询参数（Query）**：`fileId`（文件ID，必填）
- **响应示例**：
```json
{
  "code": 200,
  "msg": "文件删除成功",
  "data": {
    "fileId": 1,
    "releaseSize": 2097152  // 释放的空间大小（字节）
  }
}
```
- **业务逻辑**：删除文件时同步删除服务器文件，更新空间`usedSize`（减去文件大小）

#### 3.6 热点文件查询（游客/所有用户）
- **请求方式**：GET
- **URL**：`/api/file/hot`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | page         | int     | 当前页码（默认1）             |
  | limit        | int     | 每页条数（默认10）            |
  | categoryId   | int     | 文件分类ID（可选）            |
- **响应示例**：
```json
{
  "code": 200,
  "msg": "查询成功",
  "data": {
    "total": 1,
    "pages": 1,
    "current": 1,
    "size": 10,
    "records": [
      {
        "fileId": 1,
        "fileName": "Java核心笔记.pdf",
        "fileUrl": "http://localhost:8080/upload/U2023001/Java核心笔记.pdf",
        "fileSize": 2097152,
        "fileType": "application/pdf",
        "categoryName": "文档",
        "uploadTime": "2025-01-05 14:30:00",
        "downloadCount": 50,    // 下载量（排序依据）
        "uploadUserName": "张三" // 上传用户姓名
      }
    ]
  }
}
```

#### 3.7 文件下载（更新下载量）
- **请求方式**：GET
- **URL**：`/api/file/download`
- **查询参数（Query）**：`fileId`（文件ID，必填）
- **响应**：
    - 成功：返回文件二进制流（设置`Content-Disposition: attachment; filename=xxx.pdf`），同时更新文件`downloadCount +=1`
    - 失败：返回`code=404`提示“文件不存在或已冻结”

### 4. 管理员审批接口
#### 4.1 待审批文件查询
- **请求方式**：GET
- **URL**：`/api/admin/file/pending`
- **查询参数（Query）**：
  | 参数名       | 类型    | 说明                          |
  |--------------|---------|-------------------------------|
  | page         | int     | 当前页码（默认1）             |
  | limit        | int     | 每页条数（默认10）            |
  | categoryId   | int     | 文件分类ID（可选）            |
  | uploadUserName | string | 上传用户姓名（可选，模糊查询） |
- **响应示例**：类似文件列表查询，仅返回`status=pending`的文件

#### 4.2 文件审批操作
- **请求方式**：POST
- **URL**：`/api/admin/file/approve`
- **请求体（Body）**：
```json
{
  "fileId": 1,               // 文件ID（必填）
  "approveResult": "approved", // 审批结果（approved/rejected，必填）
  "approveAdminId": "ADMIN001", // 审批管理员ID（必填）
  "remark": "内容合规，审批通过" // 审批备注（可选）
}
```
- **响应示例**（审批通过）：
```json
{
  "code": 200,
  "msg": "文件审批通过",
  "data": {
    "fileId": 1,
    "isApproved": 1,
    "status": "normal",
    "approveDate": "2025-01-06 10:00:00"
  }
}
```
- **响应示例**（审批拒绝）：
```json
{
  "code": 200,
  "msg": "文件审批拒绝",
  "data": {
    "fileId": 1,
    "isApproved": 0,
    "status": "pending",
    "approveDate": "2025-01-06 10:00:00",
    "remark": "内容不合规，暂不通过"
  }
}
```

### 5. 关注与WebSocket接口
#### 5.1 关注用户
- **请求方式**：POST
- **URL**：`/api/follow/add`
- **请求体（Body）**：
```json
{
  "followerId": "visitor001", // 关注者ID（游客/用户ID，必填）
  "followedId": "U2023001"    // 被关注者ID（注册用户ID，必填）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "关注成功",
  "data": {
    "followId": 1,
    "followerId": "visitor001",
    "followedId": "U2023001",
    "followTime": "2025-01-06 10:30:00"
  }
}
```
- **业务约束**：重复关注返回`400`提示“已关注该用户，无需重复操作”

#### 5.2 取消关注
- **请求方式**：POST
- **URL**：`/api/follow/remove`
- **请求体（Body）**：
```json
{
  "followerId": "visitor001",
  "followedId": "U2023001"
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "取消关注成功",
  "data": null
}
```

#### 5.3 WebSocket实时通知接口
- **连接URL**：`ws://localhost:8080/ws/follow?followerId={followerId}&followedId={followedId}`
- **连接参数**：
    - `followerId`：关注者ID（游客/用户ID，必填）
    - `followedId`：被关注者ID（注册用户ID，必填）
- **消息推送格式**（被关注者上传新文件并审批通过后）：
```json
{
  "type": "file_upload",
  "message": "张三上传了新文件《Java核心笔记.pdf》",
  "fileId": 1,
  "fileName": "Java核心笔记.pdf",
  "fileUrl": "http://localhost:8080/upload/U2023001/Java核心笔记.pdf",
  "pushTime": "2025-01-06 11:00:00"
}
```
- **前端处理**：接收消息后弹出通知弹窗，点击可跳转至文件下载页面

## 三、通用接口
### 1. 用户登录
- **请求方式**：POST
- **URL**：`/api/user/login`
- **请求体（Body）**：
```json
{
  "username": "student1",    // 登录用户名（必填）
  "password": "123456"       // 登录密码（必填）
}
```
- **响应示例**：
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "userId": "U2023001",
    "username": "student1",
    "role": "student",
    "realName": "张三",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." // JWT令牌，后续请求携带
  }
}
```

### 2. 用户退出登录
- **请求方式**：POST
- **URL**：`/api/user/logout`
- **请求头**：`Authorization: Bearer {token}`
- **响应示例**：
```json
{
  "code": 200,
  "msg": "退出登录成功",
  "data": null
}
```

## 四、接口错误处理规范
| 错误类型         | 状态码 | 响应示例                                                                 |
|------------------|--------|--------------------------------------------------------------------------|
| 参数缺失/格式错误 | 400    | `{"code":400,"msg":"学号不能为空","data":null}`                           |
| 资源不存在       | 404    | `{"code":404,"msg":"班级ID=1不存在","data":null}`                         |
| 权限不足         | 403    | `{"code":403,"msg":"游客无权限上传文件","data":null}`                     |
| 业务约束冲突     | 409    | `{"code":409,"msg":"班级名称已存在","data":null}`                         |
| 服务器内部错误   | 500    | `{"code":500,"msg":"文件上传失败，请联系管理员","data":null}`             |