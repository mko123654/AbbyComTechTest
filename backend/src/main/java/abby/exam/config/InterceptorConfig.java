/***********************************************************
 * @Description : 攔截器config檔案
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-18 18:58
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.config;

import abby.exam.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 攔截User下的API
        registry.addInterceptor(loginInterceptor).addPathPatterns("/api/**");
    }

}
