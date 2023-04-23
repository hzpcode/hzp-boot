package com.hzp.mq.idempotent.intercepter;


import com.hzp.mq.idempotent.annotation.IdempotentChecker;
import com.hzp.mq.idempotent.extractor.MetaInfoExtractor;
import com.hzp.mq.idempotent.handler.IdempotentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *  幂等拦截器
 *
 *  @author Yu
 *  @date 2021/3/23 23:11
 */
@Order(-1)
@Slf4j
@Aspect
@RequiredArgsConstructor
public class IdempotentInterceptor implements ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final List<MetaInfoExtractor> extractors;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Around("@annotation(idempotentChecker)")
    public Object handle(ProceedingJoinPoint point, IdempotentChecker idempotentChecker) throws Throwable{
        Object message = findMessageArgsThrExIfNull(point.getArgs());

        IdempotentHandler idempotentHandler = applicationContext.getBean(idempotentChecker.idempotentChecker());

        if (idempotentHandler.isProcessed(message)) {
            return null;
        }

        try {
            Object result = point.proceed();
            idempotentHandler.markProcessed(message, null);
            return result;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            idempotentHandler.markProcessed(message, e);
            throw e;
        }
    }

    private Object findMessageArgsThrExIfNull(Object[] args) {
        if (Objects.isNull(args)) {
            log.warn(">>>>>>>>>> @RabbitIdempotentChecker join method parameters don't contain Message, IdempotentChecker can't running!");
            throw new RuntimeException("can't find message parameter");
        }

        for (Object arg : args) {
            Optional<MetaInfoExtractor> keyExtractor = extractors.stream().filter(o -> o.isMatch(arg)).findFirst();
            if (keyExtractor.isPresent()) {
                return arg;
            }
        }

        log.warn(">>>>>>>>>>@RabbitIdempotentChecker join method parameters don't contain Message, IdempotentChecker can't running!");
        throw new RuntimeException("can't find message parameter");
    }


}
