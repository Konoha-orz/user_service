package com.pulingle.user_service.domain.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by @杨健 on 2018/4/3 12:21
 *
 * @Des: 动态实体类
 */

public class Moment implements Serializable{
    /**
     * 动态id
     */
    private long momentId;

    /**
     * 用户id
     */
    private long userId;

    /**
     * 点赞用户id列表
     */
    private String thumbUsersList;

    /**
     * 图片id列表
     */
    private String pictureList;

    /**
     * 内容
     */
    private String content;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 评论id列表
     */
    private String commentList;

    /**
     * 隐私程度
     */
    private int privacyLev;

    public long getMomentId() {
        return momentId;
    }

    public void setMomentId(long momentId) {
        this.momentId = momentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getThumbUsersList() {
        return thumbUsersList;
    }

    public void setThumbUsersList(String thumbUsersList) {
        this.thumbUsersList = thumbUsersList;
    }

    public String getPictureList() {
        return pictureList;
    }

    public void setPictureList(String pictureList) {
        this.pictureList = pictureList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCommentList() {
        return commentList;
    }

    public void setCommentList(String commentList) {
        this.commentList = commentList;
    }

    public int getPrivacyLev() {
        return privacyLev;
    }

    public void setPrivacyLev(int privacyLev) {
        this.privacyLev = privacyLev;
    }

    @Override
    public String toString() {
        return "Moment{" +
                "momentId=" + momentId +
                ", userId=" + userId +
                ", thumbUsersList='" + thumbUsersList + '\'' +
                ", pictureList='" + pictureList + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", commentList='" + commentList + '\'' +
                ", privacyLev=" + privacyLev +
                '}';
    }
}
