package com.pulingle.user_service.web;

import com.pulingle.user_service.domain.entity.dto.RespondBody;
import com.pulingle.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
     * @return json格式的msg 登陆响应
     */
    @RequestMapping("/login")
    public Map<String, Object> checkUser(String account, String password){
        return userService.login(account,password);
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

//    /**
//     * @param friendAccount 添加好友的账号
//     * @return json格式的msg添加好友响应
//     */
//    @RequestMapping("/addFriend")
//    public Map<String,Object> addFriend(String friendAccount){
//        return userService.addFriend(friendAccount);
//    }

    @RequestMapping("/acceptFriendRequest")
    public RespondBody acceptFriendRequest(long userId,long friendId,long messageId){
        return userService.acceptFriendRequest(userId,friendId,messageId);
    }

}
