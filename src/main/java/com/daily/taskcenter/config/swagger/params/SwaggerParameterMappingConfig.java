package com.daily.taskcenter.config.swagger.params;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author z
 * @date 2019/7/16 17:52
 **/
@Configuration
public class SwaggerParameterMappingConfig {

    @Bean
    public SwaggerParameterMapingInfo timestampMappingString() {
        return new SwaggerParameterMapingInfo() {
            @Override
            public Class fromClass() {
                return Timestamp.class;
            }

            @Override
            public Class toClass() {
                return String.class;
            }
        };
    }

    @Bean
    public SwaggerParameterMapingInfo localDateTimeMappingString() {
        return new SwaggerParameterMapingInfo() {
            @Override
            public Class fromClass() {
                return LocalDateTime.class;
            }

            @Override
            public Class toClass() {
                return String.class;
            }
        };
    }

    @Bean
    public SwaggerParameterMapingInfo localDateMappingString() {
        return new SwaggerParameterMapingInfo() {
            @Override
            public Class fromClass() {
                return LocalDate.class;
            }

            @Override
            public Class toClass() {
                return String.class;
            }
        };
    }

    @Bean
    public SwaggerParameterMapingInfo localTimeMappingString() {
        return new SwaggerParameterMapingInfo() {
            @Override
            public Class fromClass() {
                return LocalTime.class;
            }

            @Override
            public Class toClass() {
                return String.class;
            }
        };
    }

    @Bean
    public SwaggerParameterMapingInfo utilDateMappingString() {
        return new SwaggerParameterMapingInfo() {
            @Override
            public Class fromClass() {
                return Date.class;
            }

            @Override
            public Class toClass() {
                return String.class;
            }
        };
    }

    @Bean
    public SwaggerParameterMapingInfo sqlDateMappingString() {
        return new SwaggerParameterMapingInfo() {
            @Override
            public Class fromClass() {
                return java.sql.Date.class;
            }

            @Override
            public Class toClass() {
                return String.class;
            }
        };
    }

}
