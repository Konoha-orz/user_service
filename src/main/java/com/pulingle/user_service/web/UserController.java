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
     * @param nickname 昵称
     * @return json格式的msg 注册响应
     */
    @RequestMapping("/register")
    public Map<String,Object> register(String account,String password,String nickname){
        return userService.register(account,password,nickname);
    }

}
