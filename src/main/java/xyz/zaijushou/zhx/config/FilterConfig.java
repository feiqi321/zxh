package xyz.zaijushou.zhx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.zaijushou.zhx.filter.OperationLogFilter;

@Configuration
public class FilterConfig {

    @Bean
    public OperationLogFilter operationLogFilter() {
        return new OperationLogFilter();
    }

}
