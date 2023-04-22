package com.hzp.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzp.core.mapper.BaseInjectMapper;

/**
 * @author huangzhengpeng
 * @Date 2022/5/29 22:32
 */
public class BaseServiceImpl<M extends BaseInjectMapper<T>,T> extends ServiceImpl<M,T> {


}
