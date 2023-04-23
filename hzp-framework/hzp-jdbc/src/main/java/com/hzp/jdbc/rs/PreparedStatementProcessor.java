package com.hzp.jdbc.rs;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *  PreparedStatement处理器
 *
 *  @author Yu
 *  @date 2021/3/23 0:21
 */
@FunctionalInterface
public interface PreparedStatementProcessor {

    void process(PreparedStatement ps) throws SQLException;
}
