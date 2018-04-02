package com.pulingle.user_service.domain.entity;

/**
 * @Author: Teemo
 * @Description:用户表
 * @Date: Created in 9:43 2018/3/25
 */
public class User {
    /**
     * 用户id
     */
    private int user_id;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

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

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
