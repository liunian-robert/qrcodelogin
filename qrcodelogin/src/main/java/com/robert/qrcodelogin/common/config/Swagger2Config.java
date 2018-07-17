package com.robert.qrcodelogin.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: hearain
 * Email: 18353367683@163.com
 * Date: 2017-06-28 20:54
 * Version: 1.0.0
 * Description:SpringMVC集成Swagger
 */
@EnableWebMvc
@EnableSwagger2
@ComponentScan(basePackages = {"com.robert.qrcodelogin"})
@Configuration
public class Swagger2Config extends WebMvcConfigurationSupport{

    @Bean
    public Docket createRestApi() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        //http header加额外请求头进行一些验证
        /*
        tokenPar.name("token").description("权限token,json字符串,{\"projectId\":\"0\",\"epId\":\"923\"}").
                modelRef(new ModelRef("string")).parameterType("header").
                required(false).build();
        pars.add(tokenPar.build());*/
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.robert.qrcodelogin"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("二维码扫描登录相关接口及其说明")
                .description("可用的REST服务页面")
                .termsOfServiceUrl("http://localhost:8080")
                .version("1.0.0")
                .build();
    }

}