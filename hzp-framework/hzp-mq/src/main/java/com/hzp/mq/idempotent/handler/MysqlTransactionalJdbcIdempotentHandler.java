
package com.hzp.mq.idempotent.handler;

import com.hzp.mq.idempotent.extractor.MetaInfoExtractor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  事务性幂等处理器
 *
 *  @author Yu
 *  @date 2021/3/28 23:56
 */
public class MysqlTransactionalJdbcIdempotentHandler extends AbstractIdempotentHandler {
    private final DataSourceTransactionManager transactionManager;
    private final AbstractIdempotentHandler idempotentChecker;

    private static final ThreadLocal<TransactionStatus> currentStatus = new ThreadLocal<>();

    public MysqlTransactionalJdbcIdempotentHandler(DataSourceTransactionManager transactionManager,
                                                   List<MetaInfoExtractor> metaInfoExtractors,
                                                   AbstractIdempotentHandler idempotentChecker) {
        super(metaInfoExtractors);
        this.transactionManager = transactionManager;
        this.idempotentChecker = idempotentChecker;
    }

    @Override
    protected boolean doIsProcessed(Object message) throws Exception {
        currentStatus.set(this.transactionManager.getTransaction(new DefaultTransactionAttribute()));
        return idempotentChecker.doIsProcessed(message);
    }

    @Override
    protected void markFailed(Object message) {
        TransactionStatus status = currentStatus.get();
        this.transactionManager.rollback(status);
    }

    @Override
    protected void markProcessed(Object message) {
        TransactionStatus status = currentStatus.get();
        this.transactionManager.commit(status);
    }

    @Override
    public void garbageCollect(LocalDateTime before) {
        this.idempotentChecker.garbageCollect(before);
    }
}
