package com.hzp.mq.idempotent.annotation;


import com.hzp.mq.idempotent.handler.IdempotentHandler;
import com.hzp.mq.idempotent.handler.MysqlTransactionalJdbcIdempotentHandler;

import java.lang.annotation.*;

/**
 * RabbitMQ幂等注解
 *
 * @author Yu
 * @date 2021/03/22 23:55
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface IdempotentChecker {

    /**
     * 幂等检查器className
     */
    Class<? extends IdempotentHandler> idempotentChecker() default MysqlTransactionalJdbcIdempotentHandler.class;


}
