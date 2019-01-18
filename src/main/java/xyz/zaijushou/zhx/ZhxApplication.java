package xyz.zaijushou.zhx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
//@EnableScheduling
//@ServletComponentScan({"cn.qiuyiping.wechat.filter"})
@MapperScan("xyz.zaijushou.zhx.**.mapper")
@EnableSwagger2
public class ZhxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhxApplication.class, args);
    }

}

