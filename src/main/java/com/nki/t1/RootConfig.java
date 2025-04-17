package com.nki.t1;

import com.nki.t1.filter.MultipartExceptionHandlingFilter;
import com.nki.t1.resolver.CustomMultipartResolver;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@PropertySource("classpath:/properties/application-file.properties")
@PropertySource("classpath:/properties/application-variable.properties")
@PropertySource("classpath:/properties/application-oauth.properties")
@PropertySource("classpath:/properties/application-aws.properties")
@EnableTransactionManagement
@EnableScheduling
@ComponentScan(basePackages = {"com.nki.t1.service", "com.nki.t1.dao", "com.nki.t1.utils", "com.nki.t1.component", "com.nki.t1.config", "com.nki.t1.filter", "com.nki.t1.utils"})
public class RootConfig {

/*    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        dataSource.setUrl("jdbc:log4jdbc:mysql://localhost:3306/t1?useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("nki");
        dataSource.setPassword("1234");
        return dataSource;
    }*/
//   ===== AWS 설정 시작 =====
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        dataSource.setUrl("jdbc:log4jdbc:mysql://awseb-e-bbxgeqfvqg-stack-awsebrdsdatabase-ypnch0du08db.c3s8ay0m4ol5.ap-northeast-2.rds.amazonaws.com:3306/t1?useUnicode=true&characterEncoding=utf8");
        dataSource.setUsername("admin");
        dataSource.setPassword("12341234");
        return dataSource;
    }
    //   ===== AWS 설정 끝 =====
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean
    public SqlSessionTemplate sqlSession() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory().getObject());
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CustomMultipartResolver multipartResolver = new CustomMultipartResolver();
        multipartResolver.setMaxUploadSize(2 * 1024 * 1024);
        return multipartResolver;
    }

    @Bean
    public MultipartExceptionHandlingFilter multipartExceptionHandlingFilter() {
        return new MultipartExceptionHandlingFilter();
    }

    @Bean(name = "path")
    public Properties pathProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(new ClassPathResource("properties/application-file.properties").getInputStream());
        return properties;
    }

}