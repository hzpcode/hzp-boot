package com.hzp.jdbc.rs;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  ResultSet转换器
 *
 *  @author Yu
 *  @date 2021/3/23 0:11
 */
@FunctionalInterface
public interface ResultSetConverter<T> {

    T convert(ResultSet resultSet) throws SQLException;
}
