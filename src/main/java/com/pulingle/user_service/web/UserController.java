package com.pulingle.user_service.web;

import com.pulingle.user_service.domain.dto.RespondBody;
import com.pulingle.user_service.domain.dto.UserLoginDTO;
import com.pulingle.user_service.domain.dto.UserRegisterDTO;
import com.pulingle.user_service.domain.entity.User;
import com.pulingle.user_service.domain.entity.User_info;
import com.pulingle.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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



    @RequestMapping("/user/login")
    public RespondBody checkUser(@RequestBody UserLoginDTO userLoginDTO){
        return userService.login(userLoginDTO.getAccount(),userLoginDTO.getPassword(),userLoginDTO.getCaptcha(),userLoginDTO.getTimeStamp());
    }

    /**
     * @param userId 调用接口的用户id
     * @param friendId 请求添加好友的用户id
     * @param messageId 好友请求消息id
     * @return 返回体
     * 接受好友请求，并删除好友请求消息记录
     */
    @RequestMapping("/acceptFriendRequest")
    public RespondBody acceptFriendRequest(long userId, long friendId, long messageId){
        return userService.acceptFriendRequest(userId,friendId,messageId);
    }

    /**
     * @param userId 调用接口的用户id
     * @param friendId 请求对方的用户id
     * @return 返回体
     * 删除好友
     */
    @RequestMapping("/deleteFriend")
    public RespondBody deleteFriend(long userId,long friendId){
        return userService.deleteFriend(userId,friendId);
    }


    /**
     * @param userId 调用接口的用户Id
     * @return 返回体
     * 根据用户id查询用户的好友列表
     */
    @RequestMapping("/getFriendInfoList")
    public RespondBody getFriendInfoList(long userId){
        return userService.getFriendInfoList(userId);
    }

    /**
    * @param: account用户账号
    * @return: RespondBody
    * @Des: 获取登录验证码
    */
    @RequestMapping(value = "/user/getCaptcha",method = RequestMethod.POST)
    public RespondBody getCaptcha(@RequestBody UserLoginDTO user){
        return userService.getCaptcha(user.getTimeStamp());
    }

    /**
     * @param: account,password,nickname
     * @return: RespondBody
     * @Des: 用户注册
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST)
    public RespondBody register(@RequestBody UserRegisterDTO userRegisterDTO){
        return  userService.register(userRegisterDTO.getAccount(),userRegisterDTO.getPassword(),userRegisterDTO.getNickname());
    }

    /**
    * @param: user_id,nickname,sex,email,signature
    * @return: RespondBody
    * @Des: 修改用户个人资料
    */
    @RequestMapping(value = "/user/updateUserInfo",method = RequestMethod.POST)
    public RespondBody updateUserInfo(@RequestBody User_info user_info){
        return userService.updateUserInfo(user_info);
    }

    /**
    * @param: user_id,profile_picture_url
    * @return: RespondBody
    * @Des: 修改用户头像
    */
    @RequestMapping(value = "/user/saveProfilePicture",method = RequestMethod.POST)
    public RespondBody saveProfilePicture(@RequestBody User_info user_info){
        return userService.saveProfilePicture(user_info);
    }

    /**
    * @param: token
    * @return:  RespondBody
    * @Des: 根据Token获取用户信息
    */
    @RequestMapping(value = "/user/tokenResolve",method = RequestMethod.POST)
    public RespondBody tokenResolve(@RequestBody UserLoginDTO userLoginDTO){
        return userService.tokenResolve(userLoginDTO.getToken());
    }

    /**
     * @param: token
     * @return:  RespondBody
     * @Des: 根据Token获取用户信息
     */
    @RequestMapping(value = "/user/logout",method = RequestMethod.POST)
    public RespondBody logout(@RequestBody UserLoginDTO userLoginDTO){
        return userService.logout(userLoginDTO.getToken());
    }

}
