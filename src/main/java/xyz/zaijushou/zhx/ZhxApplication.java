package xyz.zaijushou.zhx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan({"xyz.zaijushou.zhx.filter"})
@MapperScan("xyz.zaijushou.zhx.**.dao")
public class ZhxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhxApplication.class, args);
    }

}

