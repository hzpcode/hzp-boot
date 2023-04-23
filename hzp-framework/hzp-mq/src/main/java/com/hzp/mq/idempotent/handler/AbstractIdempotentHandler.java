
package com.hzp.mq.idempotent.handler;




import com.hzp.mq.idempotent.extractor.MessageMetaInfo;
import com.hzp.mq.idempotent.extractor.MetaInfoExtractor;
import com.hzp.web.exception.BizException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *  抽象幂等处理器
 *
 *  @author Yu
 *  @date 2021/3/28 23:56
 */
public abstract class AbstractIdempotentHandler implements IdempotentHandler {

    private final List<MetaInfoExtractor> extractors;

    public AbstractIdempotentHandler(List<MetaInfoExtractor> extractors) {
        this.extractors = extractors;
    }

    @Override
    public final boolean isProcessed(Object message) {
        try {
            return doIsProcessed(message);
        } catch (Exception e) {
            if (isIgnoreOnFailed()) {
                return false;
            }
            throw new RuntimeException(e);
        }
    }

    protected abstract boolean doIsProcessed(Object message) throws Exception;

    @Override
    public final void markProcessed(Object message, Throwable e) {
        if (e == null) {
            markProcessed(message);
            return;
        }

        if (e instanceof Exception) {
            //忽略某些异常
            if (idempotentFor != null) {
                if (idempotentFor.isAssignableFrom(e.getClass())) {
                    markProcessed(message);
                    return;
                }
            }

            //只有指定的异常回滚
            if (retryFor != null) {
                if (retryFor.isAssignableFrom(e.getClass())) {
                    markFailed(message);
                    return;
                }
            }
            markFailed(message);
        } else {
            markProcessed(message);
        }
    }

    protected abstract void markFailed(Object message);

    protected abstract void markProcessed(Object message);

    private Class<Exception> retryFor;
    private Class<Exception> idempotentFor;
    private boolean ignoreOnFailed;

    public final void setRetryFor(Class<Exception> e) {
        this.retryFor = e;
    }

    public final void setIdempotentFor(Class<Exception> e) {
        this.idempotentFor = e;
    }

    private boolean needRetry(Exception e) {
        if (retryFor == null) {
            return true;
        }
        return (retryFor.isAssignableFrom(e.getClass()));
    }

    public final boolean isIdempotent(Exception e) {
        if (idempotentFor == null) {
            return false;
        }
        return idempotentFor.isAssignableFrom(e.getClass());
    }

    /**
     * 这个地方将subject, consumerGroup拼接上去太长了
     * 最好是能将这个长串映射为一个整型带过来
     *
     * @param message
     * @return
     */
    protected MessageMetaInfo extractMetaInfo(Object message) {

        Optional<MetaInfoExtractor> keyExtractor = extractors.stream().filter(o -> o.isMatch(message)).findFirst();
        if (!keyExtractor.isPresent()) {
            throw new BizException("使用所提供的keyFunc无法提取幂等key");
        }
        return keyExtractor.get().extract(message);
    }


    public final void setIgnoreOnFailed(boolean ignoreOnFailed) {
        this.ignoreOnFailed = ignoreOnFailed;
    }

    protected final boolean isIgnoreOnFailed() {
        return this.ignoreOnFailed;
    }

    public abstract void garbageCollect(LocalDateTime before);

}
