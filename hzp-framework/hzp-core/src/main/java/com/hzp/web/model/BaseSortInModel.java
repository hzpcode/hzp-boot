package com.hzp.web.model;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

/**
 * 基础排序字段
 *
 * @author Yu
 * @date 2020/06/07 13:54
 **/
public interface BaseSortInModel {

    String getSort();

    @ApiModelProperty(hidden = true)
    default String getSortField() {
        if (!StringUtils.isNotBlank(getSort()) && getSort().contains("-")) {
            return getSort().split("-")[0];
        }
        return null;
    }

    @ApiModelProperty(hidden = true)
    default String getSortType() {
        if (!StringUtils.isNotBlank(getSort()) && getSort().contains("-")) {
            return getSort().split("-")[1];
        }
        return null;
    }

}
