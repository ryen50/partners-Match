package com.cx.usercenter.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableOpenApi

public class SwaggerConfig {
    private static final String AUTH_HEADER_NAME = "token";

    //Knife4j扩展对象
    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                //加了ApiOperation注解的方法，才生成接口文档
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //特定包下的类，才生成接口文档
                .apis(RequestHandlerSelectors.basePackage("com.cx.usercenter.controller"))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions("default"))
                //设置全局token
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());

                //每个接口传token
                //.globalRequestParameters(globalRequestParameters());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户管理系统")
                .description("用户管理系统接口文档")
//                .termsOfServiceUrl("https://www.abc.com")
//                .contact(new Contact("Jack", "https://www.cnblogs.com/jack", "123456@qq.com"))
                .version("1.0.0")

                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        return Arrays.asList(new ApiKey(AUTH_HEADER_NAME, "auth", In.HEADER.name()));
    }

    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(SecurityContext
                .builder()
                .securityReferences(securityReferences())
                .operationSelector(operationContext -> operationContext.requestMappingPattern().startsWith("/api/"))
                .build());
        return securityContexts;
    }

    private List<SecurityReference> securityReferences() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[] {new AuthorizationScope("global", "accessEverything")};
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference(AUTH_HEADER_NAME, authorizationScopes));
        return securityReferences;
    }

    private List<RequestParameter> globalRequestParameters() {
        return Arrays.asList(new RequestParameterBuilder()
                .name(AUTH_HEADER_NAME)
                .description("access token")
                .in(ParameterType.HEADER)
                .required(false)
                .build());
    }

}