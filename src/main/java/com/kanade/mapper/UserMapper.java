package com.kanade.mapper;

import com.kanade.entity.User;
import com.kanade.entity.UserMyBatis;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectUserName(String username);
    
    @Insert("INSERT INTO user(user_id, username, password, role, real_name, gender, register_date, phone, status, last_login_time, last_login_ip, is_delete, create_time, update_time) " +
            "VALUES(#{userId}, #{username}, #{password}, #{role}, #{realName}, #{gender}, #{registerDate}, #{phone}, #{status}, #{lastLoginTime}, #{lastLoginIp}, #{isDelete}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    boolean addUser(User user);
}