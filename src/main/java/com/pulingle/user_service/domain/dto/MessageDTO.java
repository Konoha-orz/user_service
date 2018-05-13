package com.pulingle.user_service.domain.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by @杨健 on 2018/4/11 16:15
 *
 * @Des: 消息传输对象
 */

public class MessageDTO implements Serializable {
    /**
     * 消息id
     */
    private long messageId;

    /**
     * 消息类型
     */
    private int type;

    /**
     * 是否已阅0未阅，1已阅
     */
    private int readStatus;

    /**
     * 发送用户Id
     */
    private long sendUserId;

    /**
     * 接受用户Id
     */
    private long receUserId;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 删除状态,0未删除，1删除
     */
    private int deleteStatus;

    /**
     * 内容
     */
    private String content;

    /**
     * 用户ID
     */
    private long userId;

    /**
     * 好友Id
     */
    private long friendId;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public long getReceUserId() {
        return receUserId;
    }

    public void setReceUserId(long receUserId) {
        this.receUserId = receUserId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }
}
