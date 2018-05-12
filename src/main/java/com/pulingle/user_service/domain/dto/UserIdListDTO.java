package com.pulingle.user_service.domain.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by @杨健 on 2018/4/7 17:59
 *
 * @Des: 用户Id列表传输对象类
 */

public class UserIdListDTO implements Serializable {

    private List<String> idList;


    private long userId;

    /**
     * 显示好友个数
     */
    private int num;

    /**
     * 昵称
     */
    private String nickname;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
