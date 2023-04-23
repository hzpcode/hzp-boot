package com.hzp.web.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数
 *
 * @author yxy
 * @date 2021/01/11 10:14
 **/

@Data
public class Sort implements Serializable {

    private String column;

    private Boolean isAsc;

}
