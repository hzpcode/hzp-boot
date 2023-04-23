package com.hzp.mq.idempotent.extractor;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 消息键
 *
 * @author Yu
 * @date 2021/04/24 00:20
 **/

@Data
@Accessors(chain = true)
public class MessageMetaInfo {

    private String businessModule;
    private String businessKey;
    private String traceId;
    private String messageId;
    private String idempotentKey;

}
