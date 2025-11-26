package com.kanade.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {
    private static SqlSessionFactory factory;

    // 初始化SqlSessionFactory
    static {
        try {
            String resource = "sql-mapconfig.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            factory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("MyBatis初始化失败");
        }
    }

    // 获取SqlSession（手动提交事务）
    public static SqlSession getSession() {
        return factory.openSession(false); // false：关闭自动提交
    }

    // 关闭SqlSession
    public static void closeSession(SqlSession session) {
        if (session != null) {
            session.close();
        }
    }
    
    // 获取SqlSessionFactory
    public static SqlSessionFactory getSqlSessionFactory() {
        return factory;
    }
}