package com.cn.image.context;

import java.io.IOException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2020年9月14日 下午3:55:23
 */
@Configuration
@MapperScan(basePackages = "com.cn.image.dao.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
public class ReportDal {

    @Autowired
    private ResourcePatternResolver resourceLoader;

    /**
     * 默认数据库image
     * @return
     * @throws Exception
     */
    @Bean(name = "dataSource", destroyMethod = "close")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() throws Exception {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("dataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        factory.setConfigLocation(
                resourceLoader.getResource("classpath:mybatis/sqlmap-config.xml"));
        try {
            factory.setMapperLocations(
                    resourceLoader.getResources("classpath*:mybatis/*Mapper.xml"));
        } catch (IOException never) {
            throw new RuntimeException(never);
        }
        factory.afterPropertiesSet();
        SqlSessionTemplate template = new SqlSessionTemplate(factory.getObject());
        return template;
    }

}
