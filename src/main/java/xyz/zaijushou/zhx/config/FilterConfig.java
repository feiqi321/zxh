package xyz.zaijushou.zhx.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import xyz.zaijushou.zhx.filter.OperationLogFilter;

@Configuration
public class FilterConfig {

    @Bean
    public OperationLogFilter operationLogFilter() {
        return new OperationLogFilter();
    }

//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(operationLogFilter());//添加过滤器
//        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
//        registration.addInitParameter("name", "alue");//添加默认参数
//        registration.setName("operationLogFilter");//设置优先级
//        registration.setOrder(1);//设置优先级
//        return registration;
//    }
}
