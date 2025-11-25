CREATE DATABASE IF NOT EXISTS experiment_sys_unified
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE experiment_sys_unified;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =================================================================
--  1. 核心用户表 (t_user)
--  整合了 学生、教师、管理员 的所有登录和基础信息
-- =================================================================
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
                          `user_id` VARCHAR(20) NOT NULL COMMENT '账号(主键): 存学号(202301)或工号(T001)',
                          `password` VARCHAR(100) NOT NULL DEFAULT '123456' COMMENT '密码',
                          `username` VARCHAR(50) NOT NULL COMMENT '真实姓名',
                          `role` VARCHAR(10) NOT NULL COMMENT '角色标识: student / teacher / admin',
                          `register_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册日期',

    -- 下面是角色特有字段 (根据role的值，部分字段可为空)
                          `class_id` INT DEFAULT NULL COMMENT '所属班级ID (仅student有效)',
                          `title` VARCHAR(20) DEFAULT NULL COMMENT '职称 (仅teacher有效)',

                          PRIMARY KEY (`user_id`)
) ENGINE=InnoDB COMMENT='统一用户表';

-- =================================================================
--  2. 个人空间模块 (Space)
--  直接关联 t_user，逻辑最简单
-- =================================================================

-- 存储空间表
DROP TABLE IF EXISTS `t_space`;
CREATE TABLE `t_space` (
                           `space_id` INT NOT NULL AUTO_INCREMENT,
                           `user_id` VARCHAR(20) NOT NULL COMMENT '关联用户账号',
                           `total_size` BIGINT NOT NULL DEFAULT 5242880 COMMENT '总空间(5M)',
                           `used_size` BIGINT NOT NULL DEFAULT 0,
                           `remain_size` BIGINT NOT NULL DEFAULT 5242880,
                           `apply_status` TINYINT DEFAULT 0 COMMENT '0:无, 1:申请中',
                           PRIMARY KEY (`space_id`),
                           UNIQUE KEY `uk_space_user` (`user_id`),
                           CONSTRAINT `fk_space_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 文件信息表
DROP TABLE IF EXISTS `t_file_category`;
CREATE TABLE `t_file_category` (
                                   `category_id` INT NOT NULL AUTO_INCREMENT,
                                   `category_name` VARCHAR(20) NOT NULL,
                                   PRIMARY KEY (`category_id`)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file` (
                          `file_id` INT NOT NULL AUTO_INCREMENT,
                          `user_id` VARCHAR(20) NOT NULL COMMENT '上传者账号',
                          `category_id` INT NOT NULL,
                          `file_name` VARCHAR(100) NOT NULL,
                          `file_path` VARCHAR(255) NOT NULL,
                          `file_size` BIGINT NOT NULL,
                          `is_frozen` TINYINT DEFAULT 0,
                          `is_top` TINYINT DEFAULT 0,
                          `audit_status` TINYINT DEFAULT 0,
                          `download_count` INT DEFAULT 0,
                          `upload_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
                          PRIMARY KEY (`file_id`),
                          CONSTRAINT `fk_file_user` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =================================================================
--  3. 教务辅助模块 (Class / Course / Score)
-- =================================================================

-- 班级表
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class` (
                           `class_id` INT NOT NULL AUTO_INCREMENT,
                           `class_name` VARCHAR(50) NOT NULL,
                           PRIMARY KEY (`class_id`)
) ENGINE=InnoDB;

-- 课程表 (关联教师ID -> t_user.user_id)
DROP TABLE IF EXISTS `t_course`;
CREATE TABLE `t_course` (
                            `course_id` INT NOT NULL AUTO_INCREMENT,
                            `course_name` VARCHAR(50) NOT NULL,
                            `teacher_id` VARCHAR(20) DEFAULT NULL COMMENT '授课教师工号',
                            `daily_ratio` DECIMAL(3,2) DEFAULT 0.3,
                            `exam_ratio` DECIMAL(3,2) DEFAULT 0.7,
                            PRIMARY KEY (`course_id`),
                            CONSTRAINT `fk_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `t_user` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 成绩表 (关联学生ID -> t_user.user_id)
DROP TABLE IF EXISTS `t_score`;
CREATE TABLE `t_score` (
                           `score_id` INT NOT NULL AUTO_INCREMENT,
                           `student_id` VARCHAR(20) NOT NULL COMMENT '学生学号',
                           `course_id` INT NOT NULL,
                           `daily_score` DECIMAL(5,2) DEFAULT 0,
                           `exam_score` DECIMAL(5,2) DEFAULT 0,
                           `total_score` DECIMAL(5,2) DEFAULT 0,
                           `is_pass` TINYINT DEFAULT 1,
                           PRIMARY KEY (`score_id`),
                           UNIQUE KEY `uk_score` (`student_id`, `course_id`),
                           CONSTRAINT `fk_score_student` FOREIGN KEY (`student_id`) REFERENCES `t_user` (`user_id`) ON DELETE CASCADE,
                           CONSTRAINT `fk_score_course` FOREIGN KEY (`course_id`) REFERENCES `t_course` (`course_id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =================================================================
--  4. 初始化测试数据
-- =================================================================

INSERT INTO `t_class` (class_name) VALUES ('23软件工程-1班');
INSERT INTO `t_file_category` (category_name) VALUES ('文档'), ('图片');

-- 1. 初始化一名老师 (账号 T001)
INSERT INTO `t_user` (user_id, password, username, role, title)
VALUES ('T001', '123456', '李教授', 'teacher', '教授');
-- 老师自动拥有空间
INSERT INTO `t_space` (user_id) VALUES ('T001');

-- 2. 初始化一名学生 (账号 2023001)
INSERT INTO `t_user` (user_id, password, username, role, class_id)
VALUES ('2023001', '123456', '王小明', 'student', 1);
-- 学生自动拥有空间
INSERT INTO `t_space` (user_id) VALUES ('2023001');

-- 3. 初始化课程
INSERT INTO `t_course` (course_name, teacher_id) VALUES ('Java程序设计', 'T001');

SET FOREIGN_KEY_CHECKS = 1;