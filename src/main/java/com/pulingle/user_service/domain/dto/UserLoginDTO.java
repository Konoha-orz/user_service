package com.pulingle.user_service.domain.dto;

import java.io.Serializable;

/**
 * Created by @杨健 on 2018/4/13 17:52
 *
 * @Des:
 */

public class UserLoginDTO implements Serializable {
    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 登录验证码
     */
    private String captcha;

    /**
     * 登录时间戳
     */
    private String timeStamp;

    /**
     * token
     */
    private String token;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
