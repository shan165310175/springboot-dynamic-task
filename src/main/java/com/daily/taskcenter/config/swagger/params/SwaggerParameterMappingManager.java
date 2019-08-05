package com.daily.taskcenter.config.swagger.params;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * 配置参数映射关系，例如 DateTime -> String
 */
@Component
public class SwaggerParameterMappingManager {
    @Autowired
    List<SwaggerParameterMapingInfo> list = new ArrayList<>();

    public void configParameterMap(Docket docket){
        list.forEach(info->{
            Class fromClass = info.fromClass();
            Class toClass = info.toClass();
            docket.directModelSubstitute(fromClass, toClass);
        });
    }
}
