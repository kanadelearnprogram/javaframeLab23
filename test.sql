-- 1. 用户表（补充审计字段+格式约束，移除外键相关）
CREATE TABLE IF NOT EXISTS `user` (
                                      `user_id` VARCHAR(20) PRIMARY KEY COMMENT '用户ID（工号/学号：ADMIN001/T001/2023001）',
                                      `username` VARCHAR(20) NOT NULL UNIQUE COMMENT '登录用户名',
                                      `password` VARCHAR(64) NOT NULL COMMENT '密码（BCrypt加密）',
                                      `role` ENUM('admin', 'edu_admin', 'teacher', 'student') NOT NULL COMMENT '角色：管理员/教务/教师/学生',
                                      `real_name` VARCHAR(20) NOT NULL COMMENT '真实姓名',
                                      `gender` CHAR(2) DEFAULT '未知' COMMENT '性别',
                                      `register_date` DATE NOT NULL COMMENT '注册日期',
                                      `phone` VARCHAR(11) COMMENT '联系方式（11位手机号）',
                                      `status` TINYINT(1) DEFAULT 1 COMMENT '账号状态：1正常/0冻结',
                                      `last_login_time` DATETIME COMMENT '最后登录时间',
                                      `last_login_ip` VARCHAR(50) COMMENT '最后登录IP',
                                      `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                      `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      INDEX `idx_role` (`role`) COMMENT '角色索引',
                                      INDEX `idx_user_id` (`user_id`) COMMENT '用户ID索引'
    -- 手机号格式校验（仅允许11位数字，以13/14/15/17/18/19开头）

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表（所有注册角色）';

-- 2. 个人空间表（补充审计字段+申请记录，移除外键约束）
CREATE TABLE IF NOT EXISTS `space` (
                                       `space_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '空间ID',
                                       `user_id` VARCHAR(20) NOT NULL UNIQUE COMMENT '关联用户ID',
                                       `total_size` BIGINT NOT NULL DEFAULT 5242880 COMMENT '总空间（默认5M=5*1024*1024字节）',
                                       `used_size` BIGINT NOT NULL DEFAULT 0 COMMENT '已用空间',
                                       `apply_status` ENUM('none', 'pending', 'approved', 'rejected') DEFAULT 'none' COMMENT '扩展申请状态',
                                       `apply_size` BIGINT DEFAULT 0 COMMENT '申请扩展大小（字节）',
                                       `apply_time` DATETIME COMMENT '申请时间',
                                       `approve_admin_id` VARCHAR(20) COMMENT '审批管理员ID（关联user表）',
                                       `approve_date` DATETIME COMMENT '审批日期',
                                       `remark` VARCHAR(200) COMMENT '审批备注',
                                       `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                       `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       INDEX `idx_user_id` (`user_id`) COMMENT '关联用户索引',
                                       INDEX `idx_apply_status` (`apply_status`) COMMENT '申请状态索引',
    -- 申请大小必须为正数
                                       CONSTRAINT `chk_space_apply_size` CHECK (`apply_size` >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人空间表';

-- 3. 文件分类表（补充排序+审计字段，移除ENUM限制提升扩展性）
CREATE TABLE IF NOT EXISTS `file_category` (
                                               `category_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
                                               `category_name` VARCHAR(20) NOT NULL UNIQUE COMMENT '分类名称（文档/图片/相册/音乐）',
                                               `category_desc` VARCHAR(100) COMMENT '分类描述',
                                               `sort_order` INT DEFAULT 0 COMMENT '排序权重（越大越靠前）',
                                               `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                               `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                               `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                               INDEX `idx_sort_order` (`sort_order`) COMMENT '排序索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件分类表';

-- 4. 文件表（删除冗余字段+补充MD5+审计字段，移除外键约束）
CREATE TABLE IF NOT EXISTS `file` (
                                      `file_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
                                      `file_name` VARCHAR(100) NOT NULL COMMENT '文件名',
                                      `file_path` VARCHAR(255) NOT NULL COMMENT '存储路径（/upload/2023001/xxx.pdf）',
                                      `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
                                      `file_type` VARCHAR(50) COMMENT '文件类型（application/pdf/image/jpg）',
                                      `file_md5` VARCHAR(32) COMMENT '文件MD5（防重复上传）',
                                      `user_id` VARCHAR(20) NOT NULL COMMENT '上传用户ID（关联user表）',
                                      `category_id` INT NOT NULL COMMENT '关联分类ID（关联file_category表）',
                                      `upload_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
                                      `status` ENUM('pending', 'normal', 'frozen') DEFAULT 'pending' COMMENT '状态：待审批/正常/冻结',
                                      `download_count` INT NOT NULL DEFAULT 0 COMMENT '下载量',
                                      `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶：1是/0否',
                                      `version` INT DEFAULT 1 COMMENT '文件版本（默认1，更新时递增）',
                                      `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                      `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      INDEX `idx_user_id` (`user_id`) COMMENT '上传用户索引',
                                      INDEX `idx_category_id` (`category_id`) COMMENT '分类索引',
                                      INDEX `idx_download_count` (`download_count`) COMMENT '下载量索引',
                                      INDEX `idx_status` (`status`) COMMENT '状态索引',
                                      INDEX `idx_file_md5` (`file_md5`) COMMENT 'MD5防重复索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表（file_url=域名+file_path动态拼接）';

-- 5. 班级表（补充状态+年级+审计字段，移除外键约束）
CREATE TABLE IF NOT EXISTS `class` (
                                       `class_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
                                       `class_name` VARCHAR(50) NOT NULL COMMENT '班级名称（软件工程2301）',
                                       `grade` VARCHAR(10) NOT NULL COMMENT '年级（2023）',
                                       `department` VARCHAR(50) NOT NULL COMMENT '所属院系',
                                       `teacher_id` VARCHAR(20) COMMENT '班主任ID（关联user表教师）',
                                       `status` ENUM('normal', 'graduated', 'dissolved') DEFAULT 'normal' COMMENT '班级状态：正常/已毕业/已解散',
                                       `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                       `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       INDEX `idx_teacher_id` (`teacher_id`) COMMENT '班主任索引',
                                       INDEX `idx_grade` (`grade`) COMMENT '年级索引',
                                       INDEX `idx_status` (`status`) COMMENT '状态索引',
    -- 唯一约束：同院系+同年级+班级名称唯一（允许不同院系同名班级）
                                       UNIQUE KEY `uk_dept_grade_class` (`department`, `grade`, `class_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 6. 学生表（补充入学时间+审计字段，移除外键约束）
CREATE TABLE IF NOT EXISTS `student` (
                                         `student_id` VARCHAR(20) PRIMARY KEY COMMENT '学号（主键：2023001格式）',
                                         `user_id` VARCHAR(20) NOT NULL UNIQUE COMMENT '关联用户表ID（学生账号）',
                                         `class_id` INT NOT NULL COMMENT '所属班级ID（关联class表）',
                                         `status` ENUM('normal', 'retake') DEFAULT 'normal' COMMENT '状态：正常/重修',
                                         `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                         `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         INDEX `idx_user_id` (`user_id`) COMMENT '关联用户索引',
                                         INDEX `idx_class_id` (`class_id`) COMMENT '班级索引',
                                         INDEX `idx_status` (`status`) COMMENT '状态索引',
    -- 学号格式校验（以4位年份开头，后接数字）
                                         CONSTRAINT `chk_student_id` CHECK (`student_id` REGEXP '^[0-9]{4}[0-9]{1,16}$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表（教务专用，关联用户表获取基础信息）';

-- 7. 课程表（修复唯一约束+补充状态+审计字段，移除外键约束）
CREATE TABLE IF NOT EXISTS `course` (
                                        `course_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
                                        `course_name` VARCHAR(50) NOT NULL COMMENT '课程名称',
                                        `daily_ratio` DECIMAL(3,2) NOT NULL COMMENT '平时成绩占比（如0.3）',
                                        `exam_ratio` DECIMAL(3,2) NOT NULL COMMENT '期末成绩占比（如0.7）',
                                        `class_id` INT NOT NULL COMMENT '关联班级ID（关联class表）',
                                        `teacher_id` VARCHAR(20) NOT NULL COMMENT '授课教师ID（关联user表）',
                                        `status` ENUM('not_started', 'ongoing', 'ended') DEFAULT 'not_started' COMMENT '课程状态：未开始/进行中/已结束',
                                        `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        INDEX `idx_class_id` (`class_id`) COMMENT '班级索引',
                                        INDEX `idx_teacher_id` (`teacher_id`) COMMENT '授课教师索引',
                                        INDEX `idx_status` (`status`) COMMENT '状态索引',
    -- 唯一约束：同班级+课程名称唯一（允许不同班级同名课程）
                                        UNIQUE KEY `uk_class_course` (`class_id`, `course_name`),
    -- 成绩占比之和必须为1.0
                                        CONSTRAINT `chk_course_ratio` CHECK (`daily_ratio` + `exam_ratio` = 1.0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- 8. 成绩表（补充审计字段+成绩范围约束，移除外键约束）
CREATE TABLE IF NOT EXISTS `score` (
                                       `score_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID',
                                       `student_id` VARCHAR(20) NOT NULL COMMENT '关联学生表学号',
                                       `course_id` INT NOT NULL COMMENT '关联课程ID（关联course表）',
                                       `daily_score` DECIMAL(5,1) DEFAULT 0.0 COMMENT '平时成绩（0-100）',
                                       `exam_score` DECIMAL(5,1) DEFAULT 0.0 COMMENT '期末成绩（0-100）',
                                       `total_score` DECIMAL(5,1) DEFAULT 0.0 COMMENT '总评成绩（自动计算）',
                                       `is_pass` TINYINT(1) DEFAULT 0 COMMENT '是否及格：1是/0否',
                                       `update_user_id` VARCHAR(20) COMMENT '最后修改人ID（关联user表）',
                                       `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                       `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       INDEX `idx_student_id` (`student_id`) COMMENT '学生索引',
                                       INDEX `idx_course_id` (`course_id`) COMMENT '课程索引',
                                       INDEX `idx_total_score` (`total_score`) COMMENT '总评成绩索引',
                                       INDEX `idx_is_pass` (`is_pass`) COMMENT '及格状态索引',
    -- 联合索引：优化按学生+课程查询成绩
                                       INDEX `idx_student_course` (`student_id`, `course_id`),
    -- 唯一约束：防止同一学生同一课程重复录入成绩
                                       UNIQUE KEY `uk_student_course` (`student_id`, `course_id`),
    -- 成绩范围约束（0-100）
                                       CONSTRAINT `chk_daily_score` CHECK (`daily_score` BETWEEN 0 AND 100),
                                       CONSTRAINT `chk_exam_score` CHECK (`exam_score` BETWEEN 0 AND 100),
                                       CONSTRAINT `chk_total_score` CHECK (`total_score` BETWEEN 0 AND 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表（总评成绩/及格状态通过代码计算更新）';

-- 9. 关注表（补充关注类型+审计字段，移除外键约束）
CREATE TABLE IF NOT EXISTS `follow` (
                                        `follow_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关注ID',
                                        `follower_id` VARCHAR(20) NOT NULL COMMENT '关注者ID（游客/用户）',
                                        `follower_type` ENUM('user', 'visitor') NOT NULL COMMENT '关注者类型：注册用户/游客',
                                        `followed_id` VARCHAR(20) NOT NULL COMMENT '被关注者ID（关联user表注册用户）',
                                        `follow_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
                                        `unfollow_time` DATETIME COMMENT '取消关注时间',
                                        `follow_duration` INT COMMENT '关注时长（天，取消关注后计算）',
                                        `is_delete` TINYINT(1) DEFAULT 0 COMMENT '是否删除：1是/0否',
                                        `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        INDEX `idx_follower` (`follower_id`, `follower_type`) COMMENT '关注者索引',
                                        INDEX `idx_followed` (`followed_id`) COMMENT '被关注者索引',
    -- 唯一约束：防止重复关注
                                        UNIQUE KEY `uk_follower_followed` (`follower_id`, `followed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注表';

-- 初始化基础数据（适配优化后结构，无外键依赖仍保证数据合法性）
-- 1. 文件分类（补充排序权重）
INSERT INTO `file_category` (`category_name`, `category_desc`, `sort_order`) VALUES
                                                                                 ('文档', 'PDF/Word/Excel等格式', 10),
                                                                                 ('图片', 'JPG/PNG/GIF等格式', 20),
                                                                                 ('相册', '批量图片存储与轮播', 30),
                                                                                 ('音乐', 'MP3/WAV等音频格式', 40);

-- 2. 测试用户（管理员/教务/教师/学生）
INSERT INTO `user` (`user_id`, `username`, `password`, `role`, `real_name`, `gender`, `register_date`, `phone`) VALUES
                                                                                                                    ('ADMIN001', 'admin', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'admin', '系统管理员', '男', '2025-01-01', '13800138000'),
                                                                                                                    ('EDU001', 'edu_admin', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'edu_admin', '教务小李', '女', '2025-01-02', '13900139000'),
                                                                                                                    ('T001', 'teacher1', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'teacher', '李老师', '女', '2025-01-03', '13700137000'),
                                                                                                                    ('U2023001', 'student1', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'student', '张三', '男', '2025-01-04', '13600136000');

-- 3. 初始化个人空间（默认5M）
INSERT INTO `space` (`user_id`) VALUES ('ADMIN001'), ('EDU001'), ('T001'), ('U2023001');

-- 4. 测试班级（补充年级字段）
INSERT INTO `class` (`class_name`, `grade`, `department`, `teacher_id`) VALUES
    ('软件工程2301', '2023', '计算机学院', 'T001');

-- 5. 测试学生（补充入学时间）
INSERT INTO `student` (`student_id`, `user_id`, `class_id`, `status`, `admission_date`) VALUES
    ('2023001', 'U2023001', 1, 'normal', '2023-09-01');

-- 6. 测试课程（权重之和=1.0）
INSERT INTO `course` (`course_name`, `daily_ratio`, `exam_ratio`, `class_id`, `teacher_id`, `status`) VALUES
    ('Java程序设计', 0.3, 0.7, 1, 'T001', 'ongoing');

-- 7. 测试成绩（计算总评成绩=85*0.3+90*0.7=88.5，is_pass=1）
INSERT INTO `score` (`student_id`, `course_id`, `daily_score`, `exam_score`, `total_score`, `is_pass`, `update_user_id`) VALUES
    ('2023001', 1, 85.0, 90.0, 88.5, 1, 'T001');