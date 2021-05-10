package com.justiniano.api.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class Swagger {

    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))   //solo los controladores  con la anotacion API de swagger
                //.apis(RequestHandlerSelectors.basePackage("com.crudjwt.api.controller"))  //trae los controladores de ese paquete especifico
                //.apis(RequestHandlerSelectors.any())  //trae todos los controladores
                .paths(PathSelectors.any())
                .build()
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "CRUD JWT",
                "CRUD + JWT + SWAGGER",
                "2.0",
                "https://www.apache.org/licenses/LICENSE-2.0.html",
                new Contact("Alex F. Justiniano De la cruz",
                        "https://github.com/AlexJustiDlc",
                        "alex_justiniano_delacruz_8@hotmail.com"),
                "Apache 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0.html",
                Collections.emptyList()
        );
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global","AccessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
    }

    private SecurityContext securityContext(){
        return SecurityContext
                .builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private ApiKey apiKey(){
        return new ApiKey("JWT", "Authorization", "header");
    }

}
