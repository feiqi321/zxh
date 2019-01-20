package xyz.zaijushou.zhx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ServletComponentScan({"xyz.zaijushou.zhx.filter"})
@MapperScan("xyz.zaijushou.zhx.**.dao")
@EnableSwagger2
public class ZhxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhxApplication.class, args);
    }

}

