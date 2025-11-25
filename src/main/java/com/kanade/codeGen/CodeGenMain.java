package com.kanade.codeGen;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeGenMain {

        public static void main(String[] args) {
            //配置数据源
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/frameLab23?characterEncoding=utf-8");
            dataSource.setUsername("root");
            dataSource.setPassword("114514");

            //创建配置内容，两种风格都可以。
            GlobalConfig globalConfig = createGlobalConfigUseStyle2();
            //GlobalConfig globalConfig = createGlobalConfigUseStyle2();

            //通过 datasource 和 globalConfig 创建代码生成器
            Generator generator = new Generator(dataSource, globalConfig);

            //生成代码
            generator.generate();
        }

        public static GlobalConfig createGlobalConfigUseStyle1() {
            //创建配置内容
            GlobalConfig globalConfig = new GlobalConfig();

            //设置根包
            globalConfig.setBasePackage("com.kanade.codeGen");



            //设置生成 entity 并启用 Lombok
            globalConfig.setEntityGenerateEnable(true);
            globalConfig.setEntityWithLombok(true);
            //设置项目的JDK版本，项目的JDK为14及以上时建议设置该项，小于14则可以不设置
            globalConfig.setEntityJdkVersion(21);

            //设置生成 mapper
            globalConfig.setMapperGenerateEnable(true);

            return globalConfig;
        }

        public static GlobalConfig createGlobalConfigUseStyle2() {
            //创建配置内容
            GlobalConfig globalConfig = new GlobalConfig();

            //设置根包
            globalConfig.getPackageConfig()
                    .setBasePackage("com.kanade.codeGen");

            //设置生成 entity 并启用 Lombok
            globalConfig.enableEntity()
                    .setWithLombok(true)
                    .setJdkVersion(21);

            //设置生成 mapper
            globalConfig.enableMapper();
            globalConfig.enableMapperXml();
            globalConfig.enableService();
            globalConfig.enableServiceImpl();

            return globalConfig;
        }

}
