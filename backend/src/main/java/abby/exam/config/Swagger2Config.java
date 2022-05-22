/***********************************************************
 * @Description : Swqgger2 config檔
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-17 22:50
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api() {

        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("Access-Token").description("RESTful API 權限token，無須授權可留空值")
                .modelRef(new ModelRef("string")).parameterType("header")
                //header中的ticket參數非必填，空值也可以
                .required(false).build();
        //根據每個方法名稱知道當前方法在設置什麼參數
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改為自己的package路徑
                .apis(RequestHandlerSelectors.basePackage("abby"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AbbyComTechTest")
                .description("create by Abby Chang at 2022 based on： https://github.com/andrewgreat/antd-pro-vite-vue3")
                .termsOfServiceUrl("https://github.com/mko123654/AbbyComTechTest")
                .version("1.0")
                .contact(new Contact("Abby Chang", "https://github.com/mko123654/AbbyComTechTest", "mko123654@gmail.com"))
                .build();
    }
}