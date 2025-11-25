-- 创建数据库

-- 1. 用户表（整合所有角色，含学生基础信息）
CREATE TABLE IF NOT EXISTS `user` (
                                      `user_id` VARCHAR(20) PRIMARY KEY COMMENT '用户ID（工号/学号：ADMIN001/T001/2023001）',
                                      `username` VARCHAR(20) NOT NULL UNIQUE COMMENT '登录用户名',
                                      `password` VARCHAR(64) NOT NULL COMMENT '密码（BCrypt加密）',
                                      `role` ENUM('admin', 'edu_admin', 'teacher', 'student') NOT NULL COMMENT '角色：管理员/教务/教师/学生',
                                      `real_name` VARCHAR(20) NOT NULL COMMENT '真实姓名',
                                      `gender` CHAR(2) DEFAULT '未知' COMMENT '性别',
                                      `register_date` DATE NOT NULL COMMENT '注册日期',
                                      `phone` VARCHAR(11) COMMENT '联系方式',
                                      `status` TINYINT(1) DEFAULT 1 COMMENT '账号状态：1正常/0冻结',
                                      INDEX `idx_role` (`role`) COMMENT '角色索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表（所有注册角色）';

-- 2. 个人空间表（一对一关联用户）
CREATE TABLE IF NOT EXISTS `space` (
                                       `space_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '空间ID',
                                       `user_id` VARCHAR(20) NOT NULL UNIQUE COMMENT '关联用户ID',
                                       `total_size` BIGINT NOT NULL DEFAULT 5242880 COMMENT '总空间（默认5M=5*1024*1024字节）',
                                       `used_size` BIGINT NOT NULL DEFAULT 0 COMMENT '已用空间',
                                       `apply_status` ENUM('none', 'pending', 'approved', 'rejected') DEFAULT 'none' COMMENT '扩展申请状态',
                                       `apply_size` BIGINT DEFAULT 0 COMMENT '申请扩展大小（字节）',
                                       `approve_admin_id` VARCHAR(20) COMMENT '审批管理员ID',
                                       `approve_date` DATETIME COMMENT '审批日期',
                                       INDEX `idx_user_id` (`user_id`),
                                       INDEX `idx_apply_status` (`apply_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人空间表';

-- 3. 文件分类表
CREATE TABLE IF NOT EXISTS `file_category` (
                                               `category_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
                                               `category_name` ENUM('文档', '图片', '相册', '音乐') NOT NULL UNIQUE COMMENT '分类名称',
                                               `category_desc` VARCHAR(100) COMMENT '分类描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件分类表';

-- 4. 文件表（核心文件管理）
CREATE TABLE IF NOT EXISTS `file` (
                                      `file_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
                                      `file_name` VARCHAR(100) NOT NULL COMMENT '文件名',
                                      `file_path` VARCHAR(255) NOT NULL COMMENT '存储路径（/upload/2023001/xxx.pdf）',
                                      `file_url` VARCHAR(255) COMMENT '访问URL',
                                      `file_size` BIGINT NOT NULL COMMENT '文件大小（字节）',
                                      `file_type` VARCHAR(50) COMMENT '文件类型（application/pdf/image/jpg）',
                                      `user_id` VARCHAR(20) NOT NULL COMMENT '上传用户ID',
                                      `category_id` INT NOT NULL COMMENT '关联分类ID',
                                      `upload_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
                                      `status` ENUM('pending', 'normal', 'frozen') DEFAULT 'pending' COMMENT '状态：待审批/正常/冻结',
                                      `download_count` INT NOT NULL DEFAULT 0 COMMENT '下载量',
                                      `is_top` TINYINT(1) DEFAULT 0 COMMENT '是否置顶：1是/0否',
                                      `is_approved` TINYINT(1) DEFAULT 0 COMMENT '是否审批通过：1是/0否',
                                      INDEX `idx_user_id` (`user_id`),
                                      INDEX `idx_category_id` (`category_id`),
                                      INDEX `idx_download_count` (`download_count`),
                                      INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 5. 班级表（教务核心）
CREATE TABLE IF NOT EXISTS `class` (
                                       `class_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
                                       `class_name` VARCHAR(50) NOT NULL UNIQUE COMMENT '班级名称（软件工程2301）',
                                       `department` VARCHAR(50) NOT NULL COMMENT '所属院系',
                                       `teacher_id` VARCHAR(20) COMMENT '班主任ID（关联user表教师）',
                                       `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 6. 学生表（教务核心：移除重复字段，通过user_id关联用户表）
CREATE TABLE IF NOT EXISTS `student` (
                                         `student_id` VARCHAR(20) PRIMARY KEY COMMENT '学号（主键，不自增，教务专用标识）',
                                         `user_id` VARCHAR(20) NOT NULL UNIQUE COMMENT '关联用户表ID（学生账号）',
                                         `class_id` INT NOT NULL COMMENT '所属班级ID',
                                         `status` ENUM('normal', 'retake') DEFAULT 'normal' COMMENT '状态：正常/重修',
                                         INDEX `idx_user_id` (`user_id`) COMMENT '关联用户表索引',
                                         INDEX `idx_class_id` (`class_id`) COMMENT '班级索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表（教务专用，关联用户表获取基础信息）';

-- 7. 课程表（修复：移除CHECK约束，权重之和通过代码校验）
CREATE TABLE IF NOT EXISTS `course` (
                                        `course_id` INT PRIMARY KEY AUTO_INCREMENT COMMENT '课程ID',
                                        `course_name` VARCHAR(50) NOT NULL UNIQUE COMMENT '课程名称',
                                        `daily_ratio` DECIMAL(3,2) NOT NULL COMMENT '平时成绩占比（如0.3）',
                                        `exam_ratio` DECIMAL(3,2) NOT NULL COMMENT '期末成绩占比（如0.7）',
                                        `class_id` INT NOT NULL COMMENT '关联班级ID',
                                        `teacher_id` VARCHAR(20) NOT NULL COMMENT '授课教师ID（关联user表）',
                                        INDEX `idx_class_id` (`class_id`),
                                        INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表（权重之和=1.0需通过代码校验）';

-- 8. 成绩表（教务核心，含自动计算字段）
CREATE TABLE IF NOT EXISTS `score` (
                                       `score_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成绩ID',
                                       `student_id` VARCHAR(20) NOT NULL COMMENT '关联学生表学号',
                                       `course_id` INT NOT NULL COMMENT '关联课程ID',
                                       `daily_score` DECIMAL(5,1) DEFAULT 0.0 COMMENT '平时成绩（0-100）',
                                       `exam_score` DECIMAL(5,1) DEFAULT 0.0 COMMENT '期末成绩（0-100）',
                                       `total_score` DECIMAL(5,1) DEFAULT 0.0 COMMENT '总评成绩（自动计算）',
                                       `is_pass` TINYINT(1) DEFAULT 0 COMMENT '是否及格：1是/0否',
                                       `input_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
                                       UNIQUE KEY `uk_student_course` (`student_id`, `course_id`) COMMENT '防止重复成绩',
                                       INDEX `idx_student_id` (`student_id`),
                                       INDEX `idx_course_id` (`course_id`),
                                       INDEX `idx_total_score` (`total_score`),
                                       INDEX `idx_is_pass` (`is_pass`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成绩表';

-- 9. 关注表（支持WebSocket实时通知）
CREATE TABLE IF NOT EXISTS `follow` (
                                        `follow_id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关注ID',
                                        `follower_id` VARCHAR(20) NOT NULL COMMENT '关注者ID（游客/用户）',
                                        `followed_id` VARCHAR(20) NOT NULL COMMENT '被关注者ID（注册用户）',
                                        `follow_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
                                        UNIQUE KEY `uk_follower_followed` (`follower_id`, `followed_id`) COMMENT '防止重复关注',
                                        INDEX `idx_followed_id` (`followed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注表';

-- 初始化基础数据（适配修正后结构）
INSERT INTO `file_category` (`category_name`, `category_desc`) VALUES
                                                                   ('文档', 'PDF/Word/Excel等格式'),
                                                                   ('图片', 'JPG/PNG/GIF等格式'),
                                                                   ('相册', '批量图片存储与轮播'),
                                                                   ('音乐', 'MP3/WAV等音频格式');

-- 测试用户（管理员/教务/教师/学生）
INSERT INTO `user` (`user_id`, `username`, `password`, `role`, `real_name`, `gender`, `register_date`, `phone`) VALUES
                                                                                                                    ('ADMIN001', 'admin', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'admin', '系统管理员', '男', '2025-01-01', '13800138000'),
                                                                                                                    ('EDU001', 'edu_admin', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'edu_admin', '教务小李', '女', '2025-01-02', '13900139000'),
                                                                                                                    ('T001', 'teacher1', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'teacher', '李老师', '女', '2025-01-03', '13700137000'),
                                                                                                                    ('U2023001', 'student1', '$2a$10$EixZaYbB.rK4fl8x2qG2Qe447f4Q6R4Q8x2qG2Qe447f4Q6R4Q8', 'student', '张三', '男', '2025-01-04', '13600136000');

-- 初始化个人空间（默认5M）
INSERT INTO `space` (`user_id`) VALUES ('ADMIN001'), ('EDU001'), ('T001'), ('U2023001');

-- 测试班级、学生（关联用户表ID）
INSERT INTO `class` (`class_name`, `department`, `teacher_id`) VALUES ('软件工程2301', '计算机学院', 'T001');
INSERT INTO `student` (`student_id`, `user_id`, `class_id`, `status`) VALUES ('2023001', 'U2023001', 1, 'normal');

-- 测试课程、成绩（关联学生学号，权重之和=1.0）
INSERT INTO `course` (`course_name`, `daily_ratio`, `exam_ratio`, `class_id`, `teacher_id`) VALUES ('Java程序设计', 0.3, 0.7, 1, 'T001');
INSERT INTO `score` (`student_id`, `course_id`, `daily_score`, `exam_score`) VALUES ('2023001', 1, 85.0, 90.0);