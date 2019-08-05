package com.daily.taskcenter.config.swagger;

import com.daily.taskcenter.config.swagger.params.SwaggerParameterMappingManager;
import com.daily.taskcenter.config.swagger.params.SwaggerParameterPage;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * @author z
 * @date 2019/7/26 8:54
 **/
@Slf4j
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
@Configuration
public class SwaggerConfig {

    @Autowired
    private SwaggerProperties swaggerProperties;

    @Autowired
    private SwaggerParameterMappingManager parameterMappingManager;

    private ApiInfo apiInfo() {
        ApiInfoBuilder builder = new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .version(swaggerProperties.getVersion());
        if (swaggerProperties.getContactName() != null) {
            builder = builder.contact(new Contact(swaggerProperties.getContactName(),
                    swaggerProperties.getContactUrl(),
                    swaggerProperties.getContactEmail()));
        }
        return builder.build();
    }

    @Bean
    public Docket docket() {
        String basePack = swaggerProperties.getBasePackages();
        if (!swaggerProperties.isEnabled()) {
            basePack = "swagger.enabled.is.false";
        }

        List<Predicate<String>> excludePath = new ArrayList<>();
        for (String path : swaggerProperties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

        List<Parameter> parameters = new ArrayList<>();
        if (swaggerProperties.getGlobalParams().size() > 0) {
            ParameterBuilder builder = new ParameterBuilder();
            for (SwaggerProperties.GlobalParams globalParam : swaggerProperties.getGlobalParams()) {
                Parameter parameter = builder
                        .parameterType(globalParam.getType())
                        .name(globalParam.getName())
                        .description(globalParam.getDescription())
                        .required(globalParam.getRequired())
                        .modelRef(new ModelRef(globalParam.getParameterType()))
                        .build();// 在swagger里显示header
                parameters.add(parameter);
            }
        }

        Docket docket = new Docket(DocumentationType.SWAGGER_2);


        parameterMappingManager.configParameterMap(docket);

        if (swaggerProperties.isEnabled()) {
            docket.apiInfo(apiInfo());
        }
        if (!parameters.isEmpty()) {
            docket.globalOperationParameters(parameters);
        }

        docket.select()
                .apis(RequestHandlerSelectors.basePackage(basePack))
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath))))
                .build();

        if (swaggerProperties.getEnableOauth2() && swaggerProperties.isEnabled()) {
            docket.securityContexts(Collections.singletonList(securityContext()))
                    .securitySchemes(Arrays.asList(securitySchema()
                    ));
        }
        return docket;
    }

    private OAuth securitySchema() {
        List<AuthorizationScope> authorizationScopeList = new ArrayList();
        for (String scope : swaggerProperties.getScopes().split(",")) {
            scope = scope.trim();
            authorizationScopeList.add(new AuthorizationScope(scope, scope));
        }
        List<GrantType> grantTypes = new ArrayList<>();
        GrantType passwordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(swaggerProperties.getAccessTokenUri());
        grantTypes.add(passwordCredentialsGrant);
        return new OAuth("oauth2", authorizationScopeList, grantTypes);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        List<AuthorizationScope> authorizationScopeList = new ArrayList();
        for (String scope : swaggerProperties.getScopes().split(",")) {
            scope = scope.trim();
            authorizationScopeList.add(new AuthorizationScope(scope, scope));
        }
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[authorizationScopeList.size()];
        authorizationScopeList.toArray(authorizationScopes);

        return Collections.singletonList(new SecurityReference("oauth2", authorizationScopes));
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(null)
                .build();
    }


    //  类型转换

    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return newArrayList(newRule(resolver.resolve(Pageable.class), resolver.resolve(SwaggerParameterPage.class)));
            }
        };
    }


}
