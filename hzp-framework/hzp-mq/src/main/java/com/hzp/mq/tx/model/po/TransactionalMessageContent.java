package com.hzp.mq.tx.model.po;


import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 事务消息内容
 *
 * @author Yu
 * @date 2021/03/17 22:20
 **/

@Data
@Accessors(chain = true)
public class TransactionalMessageContent {

    private Long id;
    private Long messageId;
    private String content;

}
