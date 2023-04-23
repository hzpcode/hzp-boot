package com.hzp.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;

/**
 *  基础分页参数
 *
 *  @author yxy
 *  @date 2019/10/15 13:40
 */
@Data
@ApiModel("分页参数")
@ToString(callSuper = true)
public class BasePageInModel{

    @Range(min = 0, max = 1, message = "是否开启分页值不在范围内[0,1]")
    @ApiModelProperty(value = "是否开启分页")
    private Integer isOpenPage = 1;

    @Range(min = 0, max = 1, message = "是否开启统计值不在范围内[0,1]")
    @ApiModelProperty(value = "是否开启统计")
    private Integer isSearchCount = 1;

    @Min(value = 1, message = "页数必须为数字并且大于等于1")
    @ApiModelProperty(value = "第几页", example = "1")
    private Integer page = 1;

    @Min(value = 1, message = "每页信息数必须为数字并且大于等于1")
    @ApiModelProperty(value = "每页数量", example = "10")
    private Integer pageSize = 10;

    @ApiModelProperty(hidden = true)
    public Integer getOffset() {
        return (page - 1) * pageSize;
    }

}
