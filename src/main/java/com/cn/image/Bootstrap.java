package com.cn.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

/**
 * @Desc 
 * @author magiczrl@foxmail.com
 * @date 2018-1-5 下午2:42:23
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableEncryptableProperties
@EnableScheduling
@EnableAsync
@EnableAspectJAutoProxy
public class Bootstrap {

    /**
     * 主函数入口
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Bootstrap.class);
        app.run(args);
    }
}
