package xyz.zaijushou.zhx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootApplication
@ServletComponentScan({"xyz.zaijushou.zhx.filter"})
@MapperScan("xyz.zaijushou.zhx.**.dao")
@EnableAsync //开启异步调用
public class ZhxApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhxApplication.class, args);
    }

    @Resource
    RestTemplateBuilder restTemplateBuilder;

    /**
     * 注意：@LoadBalanced注解，使用该注解，则调用其他服务时，必须使用服务名称[http://SERVICE-XXX]，而非IP:port
     * 使用场景: 都在网关内部, 走ribbon调用
     *
     * @return
     */
    @Bean
    RestTemplate restTemplate() {
        return restTemplateBuilder.build();
    }

    @Bean
    RestTemplate nRestTemplate() {
        return restTemplateBuilder.build();
    }

}

