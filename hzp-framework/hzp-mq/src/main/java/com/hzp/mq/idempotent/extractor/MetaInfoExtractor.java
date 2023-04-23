package com.hzp.mq.idempotent.extractor;

/**
 * 元数据提取器
 *
 * @author Yu
 * @date 2021/04/23 23:58
 **/
public interface MetaInfoExtractor {

    boolean isMatch(Object message);

    MessageMetaInfo extract(Object message);

}
