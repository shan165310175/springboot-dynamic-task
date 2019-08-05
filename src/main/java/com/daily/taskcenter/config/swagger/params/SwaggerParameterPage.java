package com.daily.taskcenter.config.swagger.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author z
 * @date 2019/7/26 8:59
 **/
@ApiModel
@Data
public class SwaggerParameterPage {
    @ApiModelProperty("页码 (0..N)")
    private Integer page;

    @ApiModelProperty("每页显示的数目")
    private Integer size;

    @ApiModelProperty("以下列格式排序标准：property[,asc | desc]。 默认排序顺序为升序。 支持多种排序条件：如：id,asc")
    private List<String> sort;
}
