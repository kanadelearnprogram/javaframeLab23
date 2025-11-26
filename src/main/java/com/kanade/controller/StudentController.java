package com.kanade.controller;

import com.kanade.entity.Student;
import com.kanade.entity.User;
import com.kanade.entity.dto.StudentDTO;
import com.kanade.entity.vo.StudentVo;
import com.kanade.mapper.ClassMapper;
import com.kanade.mapper.StudentMapper;
import com.kanade.mapper.UserMapper;
import com.kanade.util.MyBatisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
@Slf4j
public class StudentController {

    /**
     * 学生 增删改查
     */
    @PostMapping("/add")
    public String addStudent(@ModelAttribute StudentDTO student) { // 加入 Model 传递数据
        SqlSession sess = null;
        try {
            sess = MyBatisUtil.getSession();
            StudentMapper studentMapper = sess.getMapper(StudentMapper.class);
            ClassMapper classMapper = sess.getMapper(ClassMapper.class);

            // 1. 校验参数（避免空指针）
            /*if (student.getStudentId() == null || student.getClassName() == null) {
                model.addAttribute("errorMsg", "学生ID和班级名不能为空！");
                return "addStudent"; // 跳转回表单页面，显示错误
            }*/

            // 2. 查找班级ID（若班级不存在，返回错误）
            Integer classId = classMapper.searchIdByName(student.getClassName());
            /*if (classId == null) {
                model.addAttribute("errorMsg", "班级不存在：" + student.getClassName());
                return "addStudent";
            }*/

            // 3. 复制属性（原逻辑不变）
            Student stu = new Student();
            BeanUtils.copyProperties(student, stu);
            stu.setClassId(classId);
            // 注意：表单中已提交 status，无需手动设置为 "normal"（覆盖用户选择）
            // stu.setStatus("normal"); // 注释掉这行，用表单提交的 status 值
            stu.setUserId("S" + stu.getStudentId());

            // 4. 插入学生和用户（关键：MyBatis 手动管理事务，必须提交！）
            studentMapper.addStudent(stu);

            UserMapper userMapper = sess.getMapper(UserMapper.class);
            User user = new User();
            BeanUtils.copyProperties(student, user);
            user.setUsername(stu.getStudentId());
            user.setPassword("123456");
            user.setUserId("S" + stu.getStudentId());
            user.setRole("student");
            user.setRealName(student.getStudentName());
            log.info(user.toString());
            userMapper.addUser(user);

            sess.commit(); // 提交事务（之前遗漏，导致数据不入库）

            // 5. 跳转成功页面，传递学生ID
            //model.addAttribute("successMsg", "添加成功！学生ID：" + stu.getStudentId());
            return "success"; // 跳转到 success.jsp

        } catch (Exception e) {
            // 6. 异常处理：回滚事务+返回错误
            if (sess != null) {
                sess.rollback(); // 回滚事务，避免脏数据
            }
            e.printStackTrace(); // 打印异常（方便排查）
            //model.addAttribute("errorMsg", "添加失败：" + e.getMessage());
            return "addStudent"; // 跳转回表单页面

        } finally {
            // 7. 关闭 SqlSession（释放资源）
            if (sess != null) {
                sess.close();
            }
        }
    }
}