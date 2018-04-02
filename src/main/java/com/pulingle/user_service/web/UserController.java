package com.pulingle.user_service.web;

import com.pulingle.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: Teemo
 * @Description:Controller
 * @Date: Created in 10:05 2018/3/25
 */

@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * @return String "controller"
     * 用于测通controller
     */
    @RequestMapping("/test")
    public String test(){
        return "controller";
    }

    /**
     * @param account 前台传来的用户账户
     * @param password 前台传来的用户密码
     * @param session Session
     * @return json格式的msg 登陆响应
     */
    @RequestMapping("/checkLogin")
    public Map<String, Object> checkUser(String account, String password, HttpSession session){
        return userService.checkUser(account,password,session);
    }

    /**
     * @param account 账号
     * @param password 密码
     * @param retype_password 再次输入密码
     * @param phone 电话
     * @param nickname 昵称
     * @param sex 性别
     * @param email 邮箱
     * @param signature 个性签名
     * @param session Session
     * @return json格式的msg 注册响应
     */
    @RequestMapping("/register")
    public Map<String,Object> register(String account,String password,String retype_password,String phone,String nickname,String sex,String email,String signature,HttpSession session){
        return userService.register(account,password,retype_password,phone,nickname,sex,email,signature,session);
    }

}
